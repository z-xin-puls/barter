/**
 * 公共JS - API调用和工具函数
 */

const API_BASE = '';

/**
 * 获取存储的 token
 */
function getToken() {
    return localStorage.getItem('token');
}

/**
 * 获取当前登录用户信息
 */
function getUserInfo() {
    const user = localStorage.getItem('userInfo');
    return user ? JSON.parse(user) : null;
}

/**
 * 检查是否登录
 */
function requireLogin() {
    if (!getToken()) {
        showToast('请先登录', 'error');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1500);
        return false;
    }
    return true;
}

/**
 * 退出登录
 */
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    window.location.href = 'login.html';
}

/**
 * 通用请求方法
 */
function request(url, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    const token = getToken();
    if (token) {
        headers['token'] = token;
    }

    const fullUrl = url.startsWith('http') ? url : API_BASE + url;

    return fetch(fullUrl, {
        ...options,
        headers
    })
    .then(response => {
        if (response.status === 401) {
            // token失效
            showToast('登录已过期，请重新登录', 'error');
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1500);
            return Promise.reject('未授权');
        }
        return response.json();
    })
    .then(data => {
        if (data.code === 200) {
            return data;
        } else {
            showToast(data.msg || '请求失败', 'error');
            return Promise.reject(data);
        }
    })
    .catch(err => {
        if (err !== '未授权' && err.code !== 200) {
            console.error('请求错误:', err);
        }
        throw err;
    });
}

/**
 * GET 请求
 */
function httpGet(url, params = {}) {
    const query = Object.keys(params)
        .filter(k => params[k] !== undefined && params[k] !== null)
        .map(k => `${encodeURIComponent(k)}=${encodeURIComponent(params[k])}`)
        .join('&');
    const fullUrl = query ? `${url}?${query}` : url;
    return request(fullUrl, { method: 'GET' });
}

/**
 * POST 请求
 */
function httpPost(url, body = {}) {
    return request(url, {
        method: 'POST',
        body: JSON.stringify(body)
    });
}

/**
 * PUT 请求
 */
function httpPut(url, body = {}) {
    return request(url, {
        method: 'PUT',
        body: JSON.stringify(body)
    });
}

/**
 * 显示 Toast 提示
 */
function showToast(msg, type = 'info') {
    // 移除已有 toast
    document.querySelectorAll('.toast').forEach(t => t.remove());

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = msg;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 2500);
}

/**
 * 格式化日期
 */
function formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    const h = String(date.getHours()).padStart(2, '0');
    const mi = String(date.getMinutes()).padStart(2, '0');
    return `${y}-${m}-${d} ${h}:${mi}`;
}

/**
 * 状态文字映射
 */
const STATUS_MAP = {
    0: '待处理',
    1: '同意',
    2: '拒绝',
};

const ITEM_STATUS_MAP = {
    1: '上架中',
    2: '已交换完成',
    3: '已下架',
};
