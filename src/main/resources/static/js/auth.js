// Authentication module
class AuthManager {
    constructor() {
        this.apiBase = "http://localhost:1000/api";
        this.token = localStorage.getItem('jwt_token');
        this.username = localStorage.getItem('username');
    }

    // Check if user is authenticated
    isAuthenticated() {
        return this.token !== null && this.token !== undefined;
    }

    // Get current token
    getToken() {
        return this.token;
    }

    // Get current username
    getUsername() {
        return this.username;
    }

    // Login function
    async login(username, password) {
        try {
            const response = await fetch(`${this.apiBase}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            if (response.ok) {
                this.token = data.token;
                this.username = data.username;
                localStorage.setItem('jwt_token', this.token);
                localStorage.setItem('username', this.username);
                return { success: true, message: data.message };
            } else {
                return { success: false, message: data.error || 'Login failed' };
            }
        } catch (error) {
            return { success: false, message: 'Network error: ' + error.message };
        }
    }

    // Register function
    async register(username, password) {
        try {
            const response = await fetch(`${this.apiBase}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            if (response.ok) {
                return { success: true, message: data.message };
            } else {
                return { success: false, message: data.error || 'Registration failed' };
            }
        } catch (error) {
            return { success: false, message: 'Network error: ' + error.message };
        }
    }

    // Validate token
    async validateToken() {
        if (!this.token) {
            return false;
        }

        try {
            const response = await fetch(`${this.apiBase}/auth/validate`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`
                }
            });

            const data = await response.json();
            return data.valid;
        } catch (error) {
            console.error('Token validation error:', error);
            return false;
        }
    }

    // Logout function
    logout() {
        this.token = null;
        this.username = null;
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('username');
        this.showLoginModal();
    }

    // Show login modal
    showLoginModal() {
        document.getElementById('loginModal').classList.remove('hidden');
    }

    // Hide login modal
    hideLoginModal() {
        document.getElementById('loginModal').classList.add('hidden');
    }

    // Show register modal
    showRegisterModal() {
        document.getElementById('registerModal').classList.remove('hidden');
    }

    // Hide register modal
    hideRegisterModal() {
        document.getElementById('registerModal').classList.add('hidden');
    }

    // Get headers for authenticated requests
    getAuthHeaders() {
        return {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.token}`
        };
    }
}

// Global auth manager instance
const authManager = new AuthManager();

// Login form handler
async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    const result = await authManager.login(username, password);
    
    if (result.success) {
        authManager.hideLoginModal();
        showMessage('Login successful!', 'success');
        updateAuthUI();
        // Refresh car data if on car page
        if (typeof fetchCars === 'function') {
            fetchCars();
        }
    } else {
        showMessage(result.message, 'error');
    }
}

// Register form handler
async function handleRegister(e) {
    e.preventDefault();
    const username = document.getElementById('registerUsername').value;
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
        showMessage('Passwords do not match', 'error');
        return;
    }

    const result = await authManager.register(username, password);
    
    if (result.success) {
        authManager.hideRegisterModal();
        showMessage('Registration successful! Please login.', 'success');
        authManager.showLoginModal();
    } else {
        showMessage(result.message, 'error');
    }
}

// Update authentication UI
function updateAuthUI() {
    const loginBtn = document.getElementById('loginBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const userInfo = document.getElementById('userInfo');

    if (authManager.isAuthenticated()) {
        if (loginBtn) loginBtn.style.display = 'none';
        if (logoutBtn) logoutBtn.style.display = 'block';
        if (userInfo) {
            userInfo.textContent = `Welcome, ${authManager.getUsername()}`;
            userInfo.style.display = 'block';
        }
    } else {
        if (loginBtn) loginBtn.style.display = 'block';
        if (logoutBtn) logoutBtn.style.display = 'none';
        if (userInfo) userInfo.style.display = 'none';
    }
}

// Show message function
function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    if (messageDiv) {
        messageDiv.textContent = message;
        messageDiv.className = `p-3 rounded mb-4 ${type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`;
        messageDiv.style.display = 'block';
        
        // Hide message after 5 seconds
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 5000);
    }
}

// Initialize authentication on page load
document.addEventListener('DOMContentLoaded', async () => {
    updateAuthUI();
    
    // Validate token on page load
    if (authManager.isAuthenticated()) {
        const isValid = await authManager.validateToken();
        if (!isValid) {
            authManager.logout();
            showMessage('Session expired. Please login again.', 'error');
        }
    }
});
