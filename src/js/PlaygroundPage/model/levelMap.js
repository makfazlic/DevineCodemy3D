export class LevelMap {
    levelDescription = {};

    constructor(levelDescription) {
        this.levelDescription = levelDescription;
        this._fillMap();
    }

    _fillMap() {
        this.tilesMap = new Map();
        this.collectableMap = new Map();

        this._fillTilesMap();
        this._fillCollectablesMap();
    }

    _fillTilesMap() {
        this.levelDescription.board.grid.forEach((tile) => {
            var pos = {x: tile.posX, y: tile.posY, z: tile.posZ};
            var key = JSON.stringify(pos);
            if (this.tilesMap.get(key) === undefined) {
                this.tilesMap.set(key, []);
            }
            this.tilesMap.get(key).push({
                ...tile,
            });
        });
    }

    _fillCollectablesMap() {
        this.levelDescription.board.items.forEach((item) => {
            var pos = {x: item.posX, y: item.posY, z: item.posZ};
            var key = JSON.stringify(pos);
            this.collectableMap.set(key, item);
        });
    }

    reset() {
        this._fillCollectablesMap();
    }

    collectablesAt(position) {
        var key = JSON.stringify(position);
        return this.collectableMap.get(key);
    }

    hasCollectableAt(position, collectableType) {
        return this.collectablesAt(position)?.collectableType === collectableType;
    }

    hasAnyCollectableAt(position) {
        return this.collectablesAt(position) !== undefined;
    }

    hasTerrainAt(position) {
        var key = JSON.stringify(position);
        return this.tilesMap.get(key) !== undefined;
    }

    collectAt(position) {
        var key = JSON.stringify(position);
        return this.collectableMap.delete(key);
    }

    getCollectablesState() {
        return Array.from(this.collectableMap.values());
    }
}
