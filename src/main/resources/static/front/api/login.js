function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
}

function sendMsgApi(data) {
    return $axios({
        'url': '/user/get_code',
        'method': 'post',
        data
    })
}

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

  