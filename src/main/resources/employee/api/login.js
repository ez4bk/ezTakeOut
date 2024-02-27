function loginApi(data) {
    return $axios({
        'url': '/api/employee/login',
        'method': 'post',
        data
    })
}

function logoutApi() {
    return $axios({
        'url': '/api/employee/logout',
        'method': 'post',
    })
}
