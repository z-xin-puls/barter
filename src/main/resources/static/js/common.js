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
        headers['Authorization'] = 'Bearer ' + token;
    }

    const fullUrl = url.startsWith('http') ? url : API_BASE + url;

    return fetch(fullUrl, {
        ...options,
        headers
    })
    .then(response => {
        if (response.status === 401) {
            showToast('登录已过期，请重新登录', 'error');
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            // 管理员页面跳管理员登录页，普通用户跳用户登录页
            const isAdminPage = window.location.pathname.includes('admin');
            setTimeout(() => {
                window.location.href = isAdminPage ? 'admin-login.html' : 'login.html';
            }, 1500);
            return Promise.reject('未授权');
        }
        if (response.status === 403) {
            return response.json();
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
        .filter(k => params[k] !== undefined && params[k] !== null && params[k] !== '')
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
 * DELETE 请求
 */
function httpDelete(url) {
    return request(url, { method: 'DELETE' });
}

/**
 * 文件上传请求（不设置 Content-Type，让浏览器自动设置 multipart boundary）
 * @param {string} url 上传接口地址
 * @param {File} file 文件对象
 * @param {string} fieldName 表单字段名，默认 file
 */
function uploadFile(url, file, fieldName = 'file') {
    const formData = new FormData();
    formData.append(fieldName, file);

    const headers = {};
    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }
    // 注意：不要设置 Content-Type，浏览器会自动添加 multipart/form-data; boundary=...

    return fetch(url, {
        method: 'POST',
        headers: headers,
        body: formData
    })
    .then(response => {
        if (response.status === 401) {
            showToast('登录已过期，请重新登录', 'error');
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            const isAdminPage = window.location.pathname.includes('admin');
            setTimeout(() => { window.location.href = isAdminPage ? 'admin-login.html' : 'login.html'; }, 1500);
            return Promise.reject('未授权');
        }
        return response.json();
    })
    .then(data => {
        if (data.code === 200) {
            return data;
        } else {
            showToast(data.msg || '上传失败', 'error');
            return Promise.reject(data);
        }
    });
}

/**
 * 显示 Toast 提示
 */
function showToast(msg, type = 'info') {
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
    1: '已同意',
    2: '已拒绝',
    3: '已完成'
};

const ITEM_STATUS_MAP = {
    1: '上架中',
    2: '已交换完成',
    3: '已下架'
};
