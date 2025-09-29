### Google Login Flow

1.  **Initiating Login**:
    * The user clicks the "Continue with Google" button on the login or profile page.
    * This action sends a request to the `/auth/google` endpoint.

2.  **Redirecting to Google**:
    * The `GoogleAuthServlet` handles the user's request.
    * It reads configuration details like the `Client ID` and `Redirect URI`.
    * Then, it constructs a special URL and redirects the user to Google's login page. This URL includes information about your application and the permissions it's requesting (like email and name).

3.  **Authentication and Consent**:
    * On Google's page, the user signs into their Google account (if they aren't already).
    * Google displays a consent screen, asking the user for permission to share their information (like name and email) with the application.
    * If the user agrees, Google redirects them back to your application at a "callback URI" (`/auth/google/callback`), providing an authorization code.

4.  **Processing Information from Google**:
    * The `GoogleCallbackServlet` receives this authorization code.
    * The servlet uses this code, along with the `Client ID` and `Client Secret`, to send another request to Google's servers.
    * If everything is valid, Google returns an `access token`.

5.  **Retrieving Information and Logging the User In**:
    * The servlet uses the `access token` to request the user's information from Google, including their Google ID, email, and full name.
    * The application then checks if this user already exists in its database:
        * If the user already exists, the application updates their information and logs them in.
        * If not, the application creates a new account using the information retrieved from Google.
    * Finally, the application stores the user's information in the session and redirects them to the products page, completing the login process.
