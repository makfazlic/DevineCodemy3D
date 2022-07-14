export function randomBoolean() {
  return Math.floor(Math.random() * 2) == 0 ? false : true;
}

export function randomInteger(maxValue) {
	return Math.floor(Math.random() * maxValue);
}