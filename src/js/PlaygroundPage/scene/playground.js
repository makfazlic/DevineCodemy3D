import ArrowComponent from "@/components/BackArrow";
import PlaygroundP from "@/components/Playground/PlaygroundRender.vue";
import TextPopup from "@/components/TextPopup";

import VueBasicAlert from 'vue-basic-alert';
import Blockly from 'blockly';
import {Backpack} from '@blockly/workspace-backpack';
import {ScrollOptions, ScrollBlockDragger, ScrollMetricsManager} from '@blockly/plugin-scroll-options';
import SvgBackground from "@/components/SvgBackground";
import {
    toolbox,
    initTheme,
    simplifyAllJsonBlocks,
    newFunctionCallBlock,
    renameFunctionCallBlock
} from "@/js/PlaygroundPage/scene/blockly";

let workspace;

export default {
    name: "Playground",
    components: {
        PlaygroundP,
        VueBasicAlert,
        ArrowComponent,
        TextPopup,
        SvgBackground
    },
    data() {
        return {
            levelDescription: {},
            worldDescription: {},
            worldCompleted: false,
            stopped: true,
            running: false,
            textarea: "",
            theme: {}
        };
    },

    methods: {

        showCongratsMessage() {
            this.worldCompleted = true;
        },
        closeCongratsMessage() {
            this.worldCompleted = false;
            window.location.href = "/listlevels"
        },

        /**
         * This is a private method that takes a series of
         * steps and sends them to the playground, waiting
         * for their completion (using promises).
         */
        async $_playEvents(steps) {
            await steps.reduce(async (promise, step) => {
                    await promise;
                    return this.$refs.playground.performAction({
                        type: step,
                    }, this.stopped).catch((e) => {
                        throw e
                    });
                }, Promise.resolve()
            );
        },

        /**
         * Method that is called when the user wants to go to the prevoius level of the world.
         */
        async previousLevel() {
            await this.$router.push({
                name: 'Playground',
                query: {levelNumber: (this.levelDescription.levelNumber - 1)}
            });
            await this.$router.go({name: 'Playground', query: {levelNumber: (this.levelDescription.levelNumber - 1)}});
        },

        async nextLevel() {
            await this.$router.push({
                name: 'Playground',
                query: {levelNumber: (this.levelDescription.levelNumber + 1)}
            });
            await this.$router.go({name: 'Playground', query: {levelNumber: (this.levelDescription.levelNumber + 1)}});
        },

        async stopCodeExecution() {
            this.stopped = true;
            this.running = false;
        },

        isFirstLevelInWorld() {
            return this.levelDescription.levelNumber === this.worldDescription.firstLevelNumber;
        },

        isLastLevelInWorld() {
            return this.levelDescription.levelNumber === this.worldDescription.lastLevelNumber;
        },


        shouldDisplayWorldDescription() {
            let worldPlayed = this.$cookies.get('devine-codemy_worlds-played') ? parseInt(this.$cookies.get('devine-codemy_worlds-played')) : 0;
            return this.isFirstLevelInWorld() && (worldPlayed < this.worldDescription.worldNumber);
        },

        setCookie() {
            this.$cookies.set('devine-codemy_worlds-played', Math.max(parseInt(this.$cookies.get('devine-codemy_worlds-played')), this.worldDescription.worldNumber), -1);
        },

        /**
         * This is a method that takes the input code from textarea
         * and sends them to /play route, waiting for a response from server which can be:
         * - The code is correct and the level is completed, so the backend sends back a LevelValidationDTO with the animations.
         * - The code is incorrect and the level is not completed, so the backend sends the error message without the animations.
         * - The code is correct but the level is not completed, so the backend sends the animations and the error message.
         *
         */
        async runCode() {

            let blocks = Blockly.serialization.workspaces.save(workspace); // to export the blocks to json
            if (Object.keys(blocks).length === 0) {
                this.displayAlert("warning", "NO CODE TO EXECUTE", "Please write some code");
                return;
            }
            let simplifiedBlocks;

            try { // try to simplify the blocks
                simplifiedBlocks = simplifyAllJsonBlocks(blocks);
            } catch (err) { // if the blocks are not valid, display an error message and return
                this.displayAlert("error", "BAD CODE", "The code you entered is not valid. Please check your code and try again.");
                return
            }

            const body = {
                levelNumber: this.levelDescription.levelNumber,
                program: simplifiedBlocks,
                attempt: JSON.stringify(blocks)
            };

            // reset the playground
            await this.$refs.playground.reset();
            this.stopped = false;
            this.running = true;


            // send the code to the backend to validate it
            const levelValidation = await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/play`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: "include",
                body: JSON.stringify(body),
            }).then((res) => {
                if (res.status >= 400) {
                    throw new Error(res.statusText);
                }
                return res.json()
            }).catch(() => {
                // an error occurred during the parsing in the backend
                this.displayAlert("error", "PARSING ERROR", "The code you entered is not valid. Please check your code and try again.");
            });

            // if the code is correct, display the animations
            if (!levelValidation) {
                this.running = false;
            } else if (levelValidation.errors.length > 0) {
                this.displayAlert("error", "ERROR", levelValidation.errors.join('\n'));
                await this.$_playEvents(levelValidation.animations);
            } else {
                await this.$_playEvents(levelValidation.animations);
                if (levelValidation.completed && !this.stopped) {
                    this.displayAlert("success", "LEVEL COMPLETED", "Congratulations! You have completed the level!");
                    // display congrats message if world is completed
                    if (this.isLastLevelInWorld()) {
                        this.showCongratsMessage()
                    }
                    // update cookies
                    if (this.shouldDisplayWorldDescription()) {
                        this.$cookies.set('devine-codemy_worlds-played', Math.max(parseInt(this.$cookies.get('devine-codemy_worlds-played')), this.worldDesc.worldNumber), -1);
                    }
                } else if (!this.stopped) {
                    this.displayAlert("warning", "UNLUCKY", "The code is syntactically correct, but the level is not completed. Please try again.");
                }
            }
            this.running = false;
        },

        /**
         * To show an alert
         * @param {string} type: the type of the alert: success, error, warning, info
         * @param {string} title: the message to show
         * @param {string} message: the message to show
         */
        displayAlert(type, title, message) {
            this.$refs.alert.showAlert(
                type, // There are 4 types of alert: success, info, warning, error
                message, // Message of the alert
                title // Header of the alert
            );
        },

        async initBlockly() {
            workspace = Blockly.inject('blocklyDiv',
                {
                    theme: this.theme,
                    toolbox: toolbox,
                    plugins: {
                        'blockDragger': ScrollBlockDragger,
                        'metricsManager': ScrollMetricsManager,
                    },
                    grid:
                        {
                            spacing: 20,
                            length: 3,
                            colour: '#ccc',
                            snap: true
                        },
                    move: {
                        scrollbars: {
                            horizontal: false,
                            vertical: true
                        },
                        drag: true,
                        wheel: true
                    }
                }
            )
            const wheelScroll = new ScrollOptions(workspace);
            wheelScroll.init();

            function onFunctionCreate(event) {
                if (event.type === Blockly.Events.BLOCK_CREATE && event.json.type === "function") {
                    const functionName = event.json.fields.NAME;
                    let newBlock = newFunctionCallBlock(functionName);

                    // check if the function is already in the workspace
                    if (workspace.getAllBlocks(false).find(block => block.type === "function"
                        && block.getFieldValue("NAME") === functionName
                        && block.id !== event.blockId)) {
                        workspace.getBlockById(event.blockId).setWarningText(functionName + " already exists", "NAME");
                    }

                    const functionsCategory = toolbox.contents.find(category => category.name === "Functions")
                    functionsCategory.contents.push(newBlock);
                    // update toolbox
                    workspace.updateToolbox(toolbox);
                }
            }

            function onFunctionDelete(event) {
                if (event.type === Blockly.Events.BLOCK_DELETE && event.oldJson.type === "function") {
                    const functionName = event.oldJson.fields.NAME;
                    // remove the block from the workspace
                    const blocks = workspace.getBlocksByType("functionCall_" + functionName, false);
                    blocks.forEach(block => {
                        block.dispose(true, true);
                    });

                    let functionsCategory = toolbox.contents.find(category => category.name === "Functions").contents;
                    for (let i = 0; i < functionsCategory.length; i++) {
                        if (functionsCategory[i].type === "functionCall_" + functionName) {
                            functionsCategory.splice(i, 1);
                        }
                    }
                    toolbox.contents.find(category => category.name === "Functions").contents = functionsCategory;
                    workspace.updateToolbox(toolbox);
                }
            }

            function onFunctionNameChange(event) {
                if (event.type === Blockly.Events.BLOCK_CHANGE && event.element === "field" && event.name === "NAME") {
                    if (workspace.getAllBlocks(false).find(block => block.type === "function"
                        && block.getFieldValue("NAME") === event.newValue
                        && block.id !== event.blockId)) {
                        // the function already exists
                        workspace.getBlockById(event.blockId).setWarningText(event.newValue + " already exists", "NAME");
                    } else {
                        // the function does not exist
                        workspace.getBlockById(event.blockId).setWarningText(null);
                    }

                    renameFunctionCallBlock(event.oldValue, event.newValue, workspace.getBlockById(event.blockId).warning);

                    // rename only the first block in the toolbox
                    let functionsCategory = toolbox.contents.find(category => category.name === "Functions").contents;
                    for (let block of functionsCategory) {
                        if (block.type === "functionCall_" + event.oldValue) {
                            block.type = "functionCall_" + event.newValue;
                            break;
                        }
                    }
                    toolbox.contents.find(category => category.name === "Functions").contents = functionsCategory;

                    const blocks = workspace.getAllBlocks(true).filter(block => block.type === "functionCall_" + event.oldValue);
                    blocks.forEach(block => {
                        block.type = "functionCall_" + event.newValue;
                        block.inputList[0].fieldRow[0].setValue(event.newValue);
                    });
                }
            }


            const backpack = new Backpack(workspace, {
                allowEmptyBackpackOpen: false,
                contextMenu: {
                    emptyBackpack: true,
                    removeFromBackpack: true,
                    copyToBackpack: true,
                    copyAllToBackpack: true,
                    pasteAllToBackpack: true,
                    disablePreconditionChecks: true,
                },
            });
            backpack.init();

            function setFunctionCallBlocks(block) {
                if (block.type === "function") {
                    const functionName = block.fields.NAME;
                    newFunctionCallBlock(functionName);
                }
                if (block.next) {
                    setFunctionCallBlocks(block.next.block);
                }
            }

            // fetch last attempt
            await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/stats/level/${new URLSearchParams(window.location.search).get("levelNumber")}`,
                {
                    method: "GET",
                    credentials: "include"
                })
                .then(res => {
                    if (res.status === 404) {// last attempt not found
                        throw new Error("Last attempt not found")
                    }
                    return res.json()
                })
                .then(json => {
                    // load last attempt in the workspace
                    // set up all function call blocks
                    json.blocks.blocks.forEach(block => {
                        setFunctionCallBlocks(block);
                    })
                    Blockly.serialization.workspaces.load(json, workspace);

                }).catch(() => {
                    // Last attempt not found, the workspace will remain empty
                });
            workspace.addChangeListener(onFunctionCreate);
            workspace.addChangeListener(onFunctionDelete);
            workspace.addChangeListener(onFunctionNameChange);

        },


    },
    async mounted() {
        const queryParams = new URLSearchParams(window.location.search);
        const levelNumber = queryParams.get("levelNumber");

        // set the blockly theme
        this.theme = initTheme()

        // fetch level
        await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/levels/${levelNumber}`, {
            method: 'GET',
            credentials: "include",
        })
            .then(res => res.json())
            .then(levelDescription => {
                this.levelDescription = levelDescription;
                this.$store.dispatch('setWorldTheme', this.levelDescription.levelWorld)
                if (levelDescription.allowedCommands.length > 1 && toolbox.contents[0].contents.length === 4) {
                    toolbox.contents[0].contents.push({
                        "kind": "block",
                        "type": "pullLever",
                    });
                } else if (toolbox.contents[0].contents.length !== 4) {
                    toolbox.contents[0].contents.pop();
                }
                toolbox.contents.forEach(category => {
                    category.hidden = 'true';
                    levelDescription.allowedCommands.forEach(allowedCommand => {
                        if (category.hidden === 'true') {
                            category.hidden = (category.name.toLowerCase() !== allowedCommand.name.toLowerCase()).toString();
                        }
                    })
                })
                this.initBlockly();
            })
            .catch(err => {
                // redirect to 404
                window.location.href = "/error?statuscode=" + err.status;
            });


        // fetch world information
        await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/levels/worlds/${this.levelDescription.levelWorld}`,
            {
                method: "GET",
                credentials: "include"
            })
            .then(res => res.json())
            .then(res => {
                this.worldDescription = res
            });

        // setup cookies if not present
        if (!this.$cookies.isKey('devine-codemy_worlds-played')) {
            this.$cookies.set('devine-codemy_worlds-played', 0, -1);
        }
    }
};
