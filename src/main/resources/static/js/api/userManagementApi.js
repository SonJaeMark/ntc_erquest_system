import { apiRequest } from "./baseClient.js";

/**
 * User Management API methods.
 */

/**
 * Registers a new user via UserManagement.
 * @param {Object} data - RegisterRequestDTO
 * @returns {Promise} - RegisterResponseDTO
 */
export function registerUser(data) {
  return apiRequest("/api/user-management/register", {
    method: "POST",
    body: JSON.stringify(data),
    headers: { "Content-Type": "application/json" }
  });
}

/**
 * Toggles a user's active status.
 * @param {string} token - JWT access token (Requires ADMIN role).
 * @param {number} userId - ID of the user to toggle.
 * @returns {Promise} - Boolean indicating new active status.
 */
export function toggleUserActiveStatus(token, userId) {
  return apiRequest(`/api/user-management/toggle-active/${userId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    }
  });
}

/**
 * Retrieves all registered users.
 * @param {string} token - JWT access token (Requires ADMIN role).
 * @returns {Promise} - List of RegisterResponseDTO objects.
 */
export function getAllUsers(token) {
  return apiRequest("/api/user-management/all", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    }
  });
}
