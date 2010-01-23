/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import hibernate.*;

/**
 *
 * @author Vladimír Řiha
 */
public class SessionManager {

    public static int logUser(String username, String password) throws Exception {
        int idA = DatabaseManager.getUserIdByUsername(username);
        int idB = DatabaseManager.getUserIdByPassword(password + username);

        if (idA == idB && idA != -1) {
            return idA;
        }
        return -1;
    }

    

}
