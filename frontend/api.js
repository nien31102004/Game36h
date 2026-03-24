// API Configuration
const API_BASE_URL = 'http://localhost:8080/api'; // Thay đổi URL backend của bạn

// Helper function for API calls
async function apiCall(endpoint, method = 'GET', data = null, token = null) {
    const url = `${API_BASE_URL}${endpoint}`;
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);
        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || 'API Error');
        }

        return result;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// ==================== AUTH API ====================

const AuthAPI = {
    // Đăng ký
    async register(userData) {
        // userData: { username, email, password, avatar }
        return apiCall('/auth/register', 'POST', userData);
    },

    // Đăng nhập
    async login(credentials) {
        // credentials: { username, password }
        const result = await apiCall('/auth/login', 'POST', credentials);
        if (result.token) {
            localStorage.setItem('token', result.token);
            localStorage.setItem('user', JSON.stringify(result.user));
        }
        return result;
    },

    // Đăng xuất
    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'index.html';
    },

    // Kiểm tra đã đăng nhập
    isLoggedIn() {
        return !!localStorage.getItem('token');
    },

    // Lấy thông tin user hiện tại
    getCurrentUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    },

    // Lấy token
    getToken() {
        return localStorage.getItem('token');
    },

    // Cập nhật profile
    async updateProfile(userData) {
        const token = this.getToken();
        return apiCall('/users/profile', 'PUT', userData, token);
    },

    // Đổi mật khẩu
    async changePassword(passwords) {
        // passwords: { currentPassword, newPassword }
        const token = this.getToken();
        return apiCall('/users/password', 'PUT', passwords, token);
    }
};

// ==================== GAMES API ====================

const GamesAPI = {
    // Lấy danh sách game
    async getGames(params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const endpoint = queryString ? `/games?${queryString}` : '/games';
        return apiCall(endpoint);
    },

    // Lấy chi tiết game
    async getGameById(gameId) {
        return apiCall(`/games/${gameId}`);
    },

    // Lấy game theo category
    async getGamesByCategory(categoryId) {
        return apiCall(`/games/category/${categoryId}`);
    },

    // Tìm kiếm game
    async searchGames(query) {
        return apiCall(`/games/search?q=${encodeURIComponent(query)}`);
    },

    // Lấy game mới nhất
    async getNewGames(limit = 8) {
        return apiCall(`/games/new?limit=${limit}`);
    },

    // Lấy game được chơi nhiều nhất
    async getPopularGames(limit = 8) {
        return apiCall(`/games/popular?limit=${limit}`);
    },

    // Lấy game nổi bật
    async getFeaturedGames(limit = 4) {
        return apiCall(`/games/featured?limit=${limit}`);
    },

    // Tăng lượt xem game
    async incrementViews(gameId) {
        return apiCall(`/games/${gameId}/views`, 'POST');
    }
};

// ==================== COMMENTS API ====================

const CommentsAPI = {
    // Lấy comments của game
    async getComments(gameId, page = 1, limit = 10) {
        return apiCall(`/games/${gameId}/comments?page=${page}&limit=${limit}`);
    },

    // Thêm comment
    async addComment(gameId, content) {
        const token = AuthAPI.getToken();
        return apiCall(`/games/${gameId}/comments`, 'POST', { content }, token);
    },

    // Xóa comment
    async deleteComment(commentId) {
        const token = AuthAPI.getToken();
        return apiCall(`/comments/${commentId}`, 'DELETE', null, token);
    },

    // Lấy comments của user hiện tại
    async getMyComments() {
        const token = AuthAPI.getToken();
        return apiCall('/users/comments', 'GET', null, token);
    }
};

// ==================== FAVORITES API ====================

const FavoritesAPI = {
    // Lấy danh sách yêu thích của user
    async getFavorites() {
        const token = AuthAPI.getToken();
        return apiCall('/users/favorites', 'GET', null, token);
    },

    // Thêm vào yêu thích
    async addFavorite(gameId) {
        const token = AuthAPI.getToken();
        return apiCall('/users/favorites', 'POST', { game_id: gameId }, token);
    },

    // Xóa khỏi yêu thích
    async removeFavorite(gameId) {
        const token = AuthAPI.getToken();
        return apiCall(`/users/favorites/${gameId}`, 'DELETE', null, token);
    },

    // Kiểm tra game có trong yêu thích không
    async isFavorite(gameId) {
        const token = AuthAPI.getToken();
        return apiCall(`/users/favorites/check/${gameId}`, 'GET', null, token);
    }
};

// ==================== RATINGS API ====================

const RatingsAPI = {
    // Lấy đánh giá của user hiện tại
    async getMyRatings() {
        const token = AuthAPI.getToken();
        return apiCall('/users/ratings', 'GET', null, token);
    },

    // Đánh giá game
    async rateGame(gameId, score) {
        // score: 1-5
        const token = AuthAPI.getToken();
        return apiCall('/ratings', 'POST', { game_id: gameId, score }, token);
    },

    // Cập nhật đánh giá
    async updateRating(ratingId, score) {
        const token = AuthAPI.getToken();
        return apiCall(`/ratings/${ratingId}`, 'PUT', { score }, token);
    },

    // Xóa đánh giá
    async deleteRating(ratingId) {
        const token = AuthAPI.getToken();
        return apiCall(`/ratings/${ratingId}`, 'DELETE', null, token);
    },

    // Lấy đánh giá trung bình của game
    async getGameRating(gameId) {
        return apiCall(`/games/${gameId}/rating`);
    }
};

// ==================== HISTORY API ====================

const HistoryAPI = {
    // Lấy lịch sử chơi của user
    async getHistory() {
        const token = AuthAPI.getToken();
        return apiCall('/users/history', 'GET', null, token);
    },

    // Thêm vào lịch sử
    async addToHistory(gameId) {
        const token = AuthAPI.getToken();
        return apiCall('/history', 'POST', { game_id: gameId }, token);
    },

    // Xóa khỏi lịch sử
    async removeFromHistory(historyId) {
        const token = AuthAPI.getToken();
        return apiCall(`/history/${historyId}`, 'DELETE', null, token);
    },

    // Xóa toàn bộ lịch sử
    async clearHistory() {
        const token = AuthAPI.getToken();
        return apiCall('/history', 'DELETE', null, token);
    }
};

// ==================== CATEGORIES API ====================

const CategoriesAPI = {
    // Lấy danh sách categories
    async getCategories() {
        return apiCall('/categories');
    },

    // Lấy chi tiết category
    async getCategoryById(categoryId) {
        return apiCall(`/categories/${categoryId}`);
    }
};

// ==================== NOTIFICATIONS API ====================

const NotificationsAPI = {
    // Lấy thông báo của user
    async getNotifications() {
        const token = AuthAPI.getToken();
        return apiCall('/users/notifications', 'GET', null, token);
    },

    // Đánh dấu đã đọc
    async markAsRead(notificationId) {
        const token = AuthAPI.getToken();
        return apiCall(`/notifications/${notificationId}/read`, 'PUT', null, token);
    },

    // Đánh dấu tất cả đã đọc
    async markAllAsRead() {
        const token = AuthAPI.getToken();
        return apiCall('/notifications/read-all', 'PUT', null, token);
    },

    // Xóa thông báo
    async deleteNotification(notificationId) {
        const token = AuthAPI.getToken();
        return apiCall(`/notifications/${notificationId}`, 'DELETE', null, token);
    }
};

// Export các API modules
window.API = {
    Auth: AuthAPI,
    Games: GamesAPI,
    Comments: CommentsAPI,
    Favorites: FavoritesAPI,
    Ratings: RatingsAPI,
    History: HistoryAPI,
    Categories: CategoriesAPI,
    Notifications: NotificationsAPI,
    baseUrl: API_BASE_URL
};
