function backendLoginPage() {
    // Redirecting to the login page of gitlab from the backend
    window.location = `${process.env.VUE_APP_CODELAND_BACKEND_HOST}/auth/login`;
}

export {backendLoginPage}