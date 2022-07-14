/**
 * This is a simple class that enables
 * cloud animation up and down (slightly).
 */
import {randomInteger} from "@/utils/random";
import {randFloat} from "three/src/math/MathUtils";

export class AnimatedObject {
  model = undefined;
  mixer = undefined;
  anims = {};
  api = {};

  constructor(model) {
    this.model = model;
  }

	loopDuration = randFloat(0.75, 1.25);
	speed = 1 + randomInteger(2);
	state = 0;

  animate(dt) {
		this.state = (this.state + dt) % this.loopDuration;
		this.model.translateZ((this.state < this.loopDuration / 2 ? 1: -1) * this.speed * dt);
  }
}