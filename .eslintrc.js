module.exports = {
	parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module'
  },
	env: {
		es6: true,
		node: true
	},
	plugins: [
		'css'
	],
  extends: [
    'eslint:recommended',
		'plugin:css/recommended',
		'plugin:vue-scoped-css/vue3-recommended',
    'plugin:vue/vue3-recommended',
  ]
}