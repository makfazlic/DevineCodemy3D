import Blockly from "blockly";

let toolbox = {
    "kind": "categoryToolbox",
    "contents": [
        {
            "kind": "category",
            "name": "Basic Commands",
            "hidden": "true",
            "categorystyle": "math_category",
            "contents": [
                {
                    "kind": "block",
                    "type": "moveForward"
                },
                {
                    "kind": "block",
                    "type": "turnLeft",
                },
                {
                    "kind": "block",
                    "type": "turnRight",
                },
                {
                    "kind": "block",
                    "type": "collectCoin",

                },
                // {
                //     "kind": "block",
                //     "type": "pullLever",
                //     "hidden": "true",
                // }
            ]
        },
        {
            "kind": "category",
            "name": "Logic",
            "categorystyle": "logic_category",
            "hidden": "true",
            "contents": [
                {
                    "kind": "block",
                    "type": "if",
                },
                {
                    "kind": "block",
                    "type": "ifElse",
                },
            ]
        },
        {
            "kind": "category",
            "name": "Conditions",
            "categorystyle": "colour_category",
            "hidden": "true",
            "contents": [
                {
                    "kind": "block",
                    "type": "logic_boolean",
                },
                {
                    "kind": "block",
                    "type": "boolean_operation",
                },
                {
                    "kind": "block",
                    "type": "not",
                },
                {
                    "kind": "block",
                    "type": "canStep"
                },
                {
                    "kind": "block",
                    "type": "containsCoin"
                },
                {
                    "kind": "block",
                    "type": "hasLever"
                }
            ]
        },
        {
            "kind": "category",
            "name": "Loops",
            "categorystyle": "loop_category",
            "hidden": "true",
            "contents": [
                {
                    "kind": "block",
                    "type": "controls_repeat",
                },
                {
                    "kind": "block",
                    "type": "while",
                }
            ]
        },
        {
            "kind": "category",
            "name": "Functions",
            "categorystyle": "procedure_category",
            "hidden": "true",
            "contents": [
                {
                    "kind": "block",
                    "type": "function",
                }
            ]
        },
    ]
};

// Theme initalization

function initTheme() {
    let workspaceBackgroundColour = getComputedStyle(document.body).getPropertyValue('--bg-contrast2')
    let toolboxBackgroundColour = getComputedStyle(document.body).getPropertyValue('--bg')
    let toolboxForegroundColour = getComputedStyle(document.body).getPropertyValue('--bg-contrast')

    Blockly.registry.unregister('theme', 'dante');
    return Blockly.Theme.defineTheme('dante', {
        'base': Blockly.Themes.Classic,
        'componentStyles': {
            'workspaceBackgroundColour': `${workspaceBackgroundColour}`,
            'toolboxBackgroundColour': `${toolboxBackgroundColour}`,
            'toolboxForegroundColour': `${toolboxForegroundColour}`,
            'flyoutBackgroundColour': `${toolboxForegroundColour}`,
            'flyoutForegroundColour': '#ccc',
            'flyoutOpacity': 1,
            'scrollbarColour': '#ccc',
            'insertionMarkerColour': '#fff',
            'insertionMarkerOpacity': 0.3,
            'scrollbarOpacity': 0.5,
            'cursorColour': '#d0d0d0',
            'blackBackground': '#333'
        }
    })
}


// BLOCKS definition -----------------------------------------------------------------


Blockly.Blocks['logic_boolean'] = {
    init: function () {
        this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown([["true", "true"], ["false", "false"]]), "BOOL");
        this.setOutput(true, "Boolean");
        this.setColour(20);
        this.setTooltip("Boolean values");
        this.setHelpUrl("");
    }
};


Blockly.Blocks['moveForward'] = {
    init: function () {
        this.appendDummyInput()
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendField("moveForward");
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(230);
        this.setTooltip("Move the robot forward");
        this.setHelpUrl("");
    }
};

Blockly.Blocks['turnLeft'] = {
    init: function () {
        this.appendDummyInput()
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendField("turnLeft");
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(230);
        this.setTooltip("Turns the robot left");
        this.setHelpUrl("");
    }
};


Blockly.Blocks['turnRight'] = {
    init: function () {
        this.appendDummyInput()
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendField("turnRight");
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(230);
        this.setTooltip("Turns the robot right");
        this.setHelpUrl("");
    }
};


Blockly.Blocks['collectCoin'] = {
    init: function () {
        this.appendDummyInput()
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendField("collectCoin");
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(230);
        this.setTooltip("Makes the robot jumps to collect a coin");
        this.setHelpUrl("");
    }
};

Blockly.Blocks['pullLever'] = {
    init: function () {
        this.appendDummyInput()
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendField("pullLever");
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(230);
        this.setTooltip("Pulls the lever if present in the current tile");
        this.setHelpUrl("");
    }
};


Blockly.Blocks['canStep'] = {
    init: function () {
        this.appendDummyInput()
            .appendField("canStep")
            .appendField(new Blockly.FieldDropdown([
                ["left", "LEFT"],
                ["right", "RIGHT"],
                ["forward", "FORWARD"],
                ["backward", "BACKWARD"]]), "direction");
        this.setOutput(true, null);
        this.setColour(20);
        this.setTooltip("Returns true if the robot can step in a given direction");
        this.setHelpUrl("");
    }
};

Blockly.Blocks['hasLever'] = {
    init: function () {
        this.appendDummyInput()
            .appendField("hasLever")
            .appendField(new Blockly.FieldDropdown([
                ["-", "any"],
                ["active", "active"],
                ["inactive", "inactive"],
                ]), "params");
        this.setOutput(true, null);
        this.setColour(20);
        this.setTooltip("Returns true if the robot can step in a given direction");
        this.setHelpUrl("");
    }
};

Blockly.Blocks['containsCoin'] = {
    init: function () {
        this.appendDummyInput()
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendField("containsCoin");
        this.setOutput(true, null);
        this.setColour(20);
        this.setTooltip("Returns whether the tile contains a coin or not");
        this.setHelpUrl("");
    }
};

const booleanOperation = {
    'type': 'logic_operation',
    'message0': '%1 %2 %3',
    'args0': [
        {
            'type': 'input_value',
            'name': 'A',
            'check': 'Boolean',
        },
        {
            'type': 'field_dropdown',
            'name': 'OP',
            'options': [
                ['and', 'and'],
                ['or', 'or'],
                ['xor', 'xor'],
            ],
        },
        {
            'type': 'input_value',
            'name': 'B',
            'check': 'Boolean',
        },
    ],
    'inputsInline': true,
    'output': 'Boolean',
    'style': 'colour_blocks',
    'helpUrl': '%{BKY_LOGIC_OPERATION_HELPURL}',
    'extensions': ['logic_op_tooltip'],
};

Blockly.Blocks['boolean_operation'] = {
    init: function () {
        this.jsonInit(booleanOperation);
        this.setTooltip(function () {
            return 'Execute a boolean operation'
        });
    }
};

const notBlock = {
    'type': 'not',
    'message0': '%{BKY_LOGIC_NEGATE_TITLE}',
    'args0': [
        {
            'type': 'input_value',
            'name': 'BOOL',
            'check': 'Boolean',
        },
    ],
    'output': 'Boolean',
    'style': 'colour_blocks',
    'tooltip': '%{BKY_LOGIC_NEGATE_TOOLTIP}',
    'helpUrl': '%{BKY_LOGIC_NEGATE_HELPURL}',
}

Blockly.Blocks['not'] = {
    init: function () {
        this.jsonInit(notBlock);
        this.setTooltip(function () {
            return 'Negates a boolean'
        });
    }
};

const whileBlock = {
    'type': 'while',
    'message0': 'repeat while %1',
    'args0': [
        {
            'type': 'input_value',
            'name': 'BOOL',
            'check': 'Boolean',
        },
    ],
    'message1': '%{BKY_CONTROLS_REPEAT_INPUT_DO} %1',
    'args1': [{
        'type': 'input_statement',
        'name': 'DO',
    }],
    'previousStatement': null,
    'nextStatement': null,
    'style': 'loop_blocks',
    'helpUrl': '%{BKY_CONTROLS_WHILEUNTIL_HELPURL}',
    'extensions': ['controls_whileUntil_tooltip'],
};

Blockly.Blocks['while'] = {
    init: function () {
        this.jsonInit(whileBlock);
        this.setTooltip(function () {
            return 'Repeat an action until the condition is true'
        });
    }
};


const ifBlock = {
    'type': 'if',
    'message0': '%{BKY_CONTROLS_IF_MSG_IF} %1',
    'args0': [
        {
            'type': 'input_value',
            'name': 'IF0',
            'check': 'Boolean',
        },
    ],
    'message1': '%{BKY_CONTROLS_IF_MSG_THEN} %1',
    'args1': [
        {
            'type': 'input_statement',
            'name': 'DO0',
        },
    ],
    'previousStatement': null,
    'nextStatement': null,
    'style': 'logic_blocks',
    'helpUrl': '%{BKY_CONTROLS_IF_HELPURL}',
    'suppressPrefixSuffix': true,
    'extensions': ['controls_if_tooltip'],
}

Blockly.Blocks['if'] = {
    init: function () {
        this.jsonInit(ifBlock);
        this.setTooltip(function () {
            return 'Perform actions if the condition is true'
        });
    }
};


const ifElseBlock = {
    'type': 'ifElse',
    'message0': '%{BKY_CONTROLS_IF_MSG_IF} %1',
    'args0': [
        {
            'type': 'input_value',
            'name': 'IF0',
            'check': 'Boolean',
        },
    ],
    'message1': '%{BKY_CONTROLS_IF_MSG_THEN} %1',
    'args1': [
        {
            'type': 'input_statement',
            'name': 'DO0',
        },
    ],
    'message2': 'else %1',
    'args2': [
        {
            'type': 'input_statement',
            'name': 'DO1',
        },
    ],
    'previousStatement': null,
    'nextStatement': null,
    'style': 'logic_blocks',
    'helpUrl': '%{BKY_CONTROLS_IF_HELPURL}',
    'suppressPrefixSuffix': true,
    'extensions': ['controls_if_tooltip'],
}

Blockly.Blocks['ifElse'] = {
    init: function () {
        this.jsonInit(ifElseBlock);
        this.setTooltip(function () {
            return 'Perform actions if the condition is true and other actions if it is not'
        });
    }
};


Blockly.Blocks['function'] = {
    init: function () {
        this.jsonInit({
            "type": "function",
            "message0": "function %1 %2 %3",
            "args0": [
                {
                    "type": "field_input",
                    "name": "NAME",
                    "text": "myFunction"
                },
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_statement",
                    "name": "STACK"
                }
            ],
            "style": "procedure_blocks",
            "tooltip": "",
            "helpUrl": ""
        });
    }
}

function newFunctionCallBlock(functionName) {
    Blockly.Blocks["functionCall_" + functionName] = {
        init: function () {
            this.appendDummyInput()
                .setAlign(Blockly.ALIGN_CENTRE)
                .appendField(functionName);
            this.setPreviousStatement(true, null);
            this.setNextStatement(true, null);
            this.setColour(290);
            this.setTooltip("Invoke the function " + functionName);
            this.setHelpUrl("");
        }
    };
    return {
        "kind": "block",
        "type": "functionCall_" + functionName,
    }
}

function renameFunctionCallBlock(oldName, newName, deleteOld = true) {
    Blockly.Blocks["functionCall_" + newName] = {
        init: function () {
            this.appendDummyInput()
                .setAlign(Blockly.ALIGN_CENTRE)
                .appendField(newName);
            this.setPreviousStatement(true, null);
            this.setNextStatement(true, null);
            this.setColour(290);
            this.setTooltip("Invoke the function " + newName);
            this.setHelpUrl("");
        }
    };
    if (deleteOld) {
        delete Blockly.Blocks["functionCall_" + oldName];
    }
    return {
        "kind": "block",
        "type": "functionCall_" + newName,
    }
}


// Functions -------------------------------------------------------------------------

/**
 * To simplify a single block.
 * Each block can have a next block, so we recursively simplify the next block as well.
 * @param block The block to simplify.
 * @returns {{}} The simplified block.
 */
function simplifyJsonBlock(block) {
    let simplifiedBlock = {};
    switch (block.type) {
        case 'moveForward':
        case 'turnLeft':
        case 'turnRight':
        case 'collectCoin':
        case 'pullLever':
            simplifiedBlock.type = block.type;
            break;
        // FUNCTIONS ---------------------------------------------------------------
        case 'function':
            simplifiedBlock.type = 'functionDefinition';
            simplifiedBlock.functionName = block.fields.NAME;
            simplifiedBlock.body = simplifyJsonBlock(block.inputs.STACK.block);
            break;
        case block.type.startsWith('functionCall_') ? block.type : '':
            simplifiedBlock.type = 'functionCall';
            simplifiedBlock.functionName = block.type.substr(block.type.indexOf('_') + 1);
            break;
        // LOOPS --------------------------------------------------------------------
        case 'while':
            simplifiedBlock.type = block.type;
            simplifiedBlock.condition = simplifyJsonBlock(block.inputs.BOOL.block);
            simplifiedBlock.body = simplifyJsonBlock(block.inputs.DO.block);
            break;
        case 'controls_repeat':
            simplifiedBlock.type = 'loop';
            simplifiedBlock.count = block.fields.TIMES;
            simplifiedBlock.body = simplifyJsonBlock(block.inputs.DO.block);
            break;
        // CONDITIONS ----------------------------------------------------------------
        case 'containsCoin':
            simplifiedBlock.type = block.type;
            break;
        case 'logic_boolean':
            simplifiedBlock.type = block.fields.BOOL.toLowerCase();
            break;
        case 'canStep':
            simplifiedBlock.type = block.type;
            simplifiedBlock.orientation = block.fields.direction;
            break;
        case 'hasLever':
            simplifiedBlock.type = block.type;
            simplifiedBlock.params = block.fields.params;
            break;
        // LOGIC ---------------------------------------------------------------------
        case 'if':
            simplifiedBlock.type = block.type;
            simplifiedBlock.condition = simplifyJsonBlock(block.inputs.IF0.block);
            simplifiedBlock.body = simplifyJsonBlock(block.inputs.DO0.block);
            break;
        case 'ifElse':
            simplifiedBlock.type = block.type;
            simplifiedBlock.condition = simplifyJsonBlock(block.inputs.IF0.block);
            simplifiedBlock.ifTrue = simplifyJsonBlock(block.inputs.DO0.block);
            simplifiedBlock.ifFalse = simplifyJsonBlock(block.inputs.DO1.block);
            break;
        case 'boolean_operation':
            simplifiedBlock.type = block.fields.OP;
            simplifiedBlock.operandA = simplifyJsonBlock(block.inputs.A.block);
            simplifiedBlock.operandB = simplifyJsonBlock(block.inputs.B.block);
            break;
        case 'not':
            simplifiedBlock.type = block.type;
            simplifiedBlock.body = simplifyJsonBlock(block.inputs.BOOL.block);
            break;
        // DEFAULT -------------------------------------------------------------------
        default:
            break;
    }

    // if there is a next block, simplify it as well
    if (block.next) {
        simplifiedBlock.next = simplifyJsonBlock(block.next.block);
    }

    return simplifiedBlock;
}


/**
 * To simplify the blockly json representing the blocks into a simpler json to be sent to the backend
 * @param blocks the blocks to simplify
 * @returns {{commands: *[]}} the simplified blocks
 */
function simplifyAllJsonBlocks(blocks) {
    let simplifiedJson = {
        "commands": [],
    };
    blocks.blocks.blocks.forEach(block => {
        simplifiedJson.commands.push(simplifyJsonBlock(block));
    });

    return simplifiedJson;
}


export {
    newFunctionCallBlock,
    renameFunctionCallBlock,
    simplifyAllJsonBlocks,
    toolbox,
    initTheme,
};

