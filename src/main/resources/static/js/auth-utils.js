/**
 * Utilitários para gerenciamento de autenticação JWT
 */

class AuthUtils {
  /**
   * Obtém o token de acesso armazenado
   */
  static getAccessToken() {
    return localStorage.getItem('accessToken');
  }

  /**
   * Obtém o refresh token armazenado
   */
  static getRefreshToken() {
    return localStorage.getItem('refreshToken');
  }

  /**
   * Armazena os tokens
   */
  static setTokens(accessToken, refreshToken) {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  /**
   * Remove os tokens (logout)
   */
  static clearTokens() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  /**
   * Decodifica o JWT e retorna o payload
   */
  static decodeToken(token) {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) {
        console.error('Token inválido');
        return null;
      }

      const payload = parts[1];
      const padding = 4 - (payload.length % 4);
      const paddedPayload = padding < 4 ? payload + '='.repeat(padding) : payload;
      const decodedPayload = atob(paddedPayload);
      return JSON.parse(decodedPayload);
    } catch (error) {
      console.error('Erro ao decodificar token:', error);
      return null;
    }
  }

  /**
   * Extrai as roles do token
   */
  static getRolesFromToken(token) {
    const claims = this.decodeToken(token);
    if (!claims) return [];

    let roles = claims.roles || claims.authorities || claims.role || [];

    if (typeof roles === 'string') {
      roles = roles.replace(/[\[\]]/g, '');
      roles = roles.split(',').map(r => r.trim()).filter(r => r.length > 0);
    }

    if (!Array.isArray(roles)) {
      roles = [roles];
    }

    return roles;
  }

  /**
   * Verifica se o token está expirado
   */
  static isTokenExpired(token) {
    const claims = this.decodeToken(token);
    if (!claims || !claims.exp) return true;

    const expirationTime = claims.exp * 1000; // Converter para millisegundos
    return Date.now() >= expirationTime;
  }

  /**
   * Verifica se o usuário está autenticado
   */
  static isAuthenticated() {
    const token = this.getAccessToken();
    return token && !this.isTokenExpired(token);
  }

  /**
   * Obtém informações do usuário do token
   */
  static getUserInfo() {
    const token = this.getAccessToken();
    if (!token) return null;

    const claims = this.decodeToken(token);
    return {
      sub: claims?.sub,
      email: claims?.email,
      name: claims?.name,
      roles: this.getRolesFromToken(token),
      iat: claims?.iat,
      exp: claims?.exp
    };
  }

  /**
   * Faz fetch com autenticação automática
   */
  static async authenticatedFetch(url, options = {}) {
    const token = this.getAccessToken();

    if (!token) {
      window.location.href = '/api/web/login';
      return null;
    }

    if (this.isTokenExpired(token)) {
      // Token expirado, tentar refresh
      const refreshed = await this.refreshAccessToken();
      if (!refreshed) {
        this.clearTokens();
        window.location.href = '/api/web/login';
        return null;
      }
    }

    const headers = {
      ...options.headers,
      'Authorization': `Bearer ${this.getAccessToken()}`
    };

    return fetch(url, { ...options, headers });
  }

  /**
   * Tenta renovar o access token usando o refresh token
   */
  static async refreshAccessToken() {
    const refreshToken = this.getRefreshToken();

    if (!refreshToken) {
      return false;
    }

    try {
      const response = await fetch('/api/users/refresh-token', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          refreshToken: refreshToken
        })
      });

      if (response.ok) {
        const data = await response.json();
        this.setTokens(data.accessToken, data.refreshTokenToken);
        return true;
      }
    } catch (error) {
      console.error('Erro ao renovar token:', error);
    }

    return false;
  }

  /**
   * Realiza logout
   */
  static logout() {
    this.clearTokens();
    window.location.href = '/api/web/login';
  }

  /**
   * Verifica se tem uma role específica
   */
  static hasRole(role) {
    const roles = this.getRolesFromToken(this.getAccessToken() || '');
    return roles.includes(role);
  }

  /**
   * Redireciona baseado na role
   */
  static redirectByRole() {
    if (!this.isAuthenticated()) {
      window.location.href = '/api/web/login';
      return;
    }

    const roles = this.getRolesFromToken(this.getAccessToken());

    if (roles.includes('ADMIN')) {
      window.location.href = '/api/web/home-manager';
    } else if (roles.includes('CLIENT')) {
      window.location.href = '/api/web/home-user';
    } else {
      window.location.href = '/api/web/home-user';
    }
  }
}

// Exportar para uso em navegadores
if (typeof module !== 'undefined' && module.exports) {
  module.exports = AuthUtils;
}

