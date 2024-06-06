// 查询列表页接口
const getOrderDetailPage = (params) => {
    return $axios({
        url: '/api/order/page',
        method: 'get',
        params
    })
}

// 查看接口
const queryOrderDetailById = (id) => {
    return $axios({
        url: `/api/orderDetail/${id}`,
        method: 'get'
    })
}

// Cancel，派送，完成接口
const editOrderDetail = (params) => {
    return $axios({
        url: '/api/order',
        method: 'put',
        data: {...params}
    })
}
