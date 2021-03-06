:title OAuth 2.0 Authentication with SPiD on iOS
:frontpage
:category api-integration
:body

One of the most compelling features of the SPiD is Single Sign-On (SSO). SSO
lets users sign into your app using their SPiD identity. If they have already
signed in to SPiD on their device, there is no need to retype the username and
password. If they have not signed in but asked the SPiD platform to remember
them (via a "remember me" cookie), they will be automatically logged in and
redirected back to your app with the oauth authentication code needed to
finalize the authentication on your app.

SSO primarily works by redirecting users to SPiD on their devices. This can be
done by opening the login/register dialogue in the Safari mobile browser. After
the user logs in, or registers and verifies their account, Safari redirects the
user back to the calling app via the app's custom URI scheme. The URL will
contain an OAuth code that the app will need to post back to SPiD, along with
the app's client credentials. In response to this, the app will receive an OAuth
access token that can be used to make authenticated API requests to SPiD on
behalf of the signed-in user.

## Webview vs Browser redirect

While an embedded webview might appear to provide a more seamless integration,
it is discouraged for several reasons:

- **Security** - The webview layer is 100% controlled by the app, opening up the
    [possibility for capturing user input and thus their SPiD credentials](http://welcome.totheinter.net/2011/01/12/stealing-passwords-is-easy-in-native-mobile-apps-despite-oauth/).
- **User convenience** - SPiD is used by many services and apps throughout
    Schibsted and SSO will only work on a mobile device if all these services
    and apps use the same authentication mechanism. At the moment this is the
    Safari mobile browser, where the "remember me" SPiD cookie is saved.
- **Browser Compatibility** - The browser engine used in webviews is not the
    same as that in the native browser. We’ve seen issues with Javascript and
    Cookies (cookies not being shared between the app and the mobile Safari
    browser).
- **Phishing** - In-app webviews makes it easier for rogue apps to succeed in
    stealing (phishing) SPiD user credentials because users cannot verify the
    origin of the login page.
- **Trust** - By always showing the same authentication process each time a user
    is signing into an app or service that uses SPiD, we assume the user will
    feel comfortable and secure that he is logging in with his SPiD account
    through a safe and recognizable manner.

## Example authentication flow

This example shows how the authentication flow works on the Fedrelandsvennen
eReader app, which uses `fvnereader://login` as its custom URI scheme. The
authentication happens on our stage platform.

1. Direct user to authorize your app on SPiD
    Example on stage server:

    ```text
    https://stage.payment.schibsted.no/login?client_id=4f6b7595efd04b2d5000000d&response_type=code&redirect_uri=fvnereader%3A%2F%2Flogin
    ```

2. After authentication, the server redirects the user back to the application with an OAuth code

    ```text
    fvnereader://login?code=55d4f8e7fbd1bd4f3e0a312c7765cecfce37c7ec
    ```

    [More about working with custom URL Schemes in iOS](http://mobile.tutsplus.com/tutorials/iphone/ios-sdk-working-with-url-schemes/).

3. Use the OAuth code to get a permanent OAuth token by `POST`-ing the code
    along with your credentials.

    Example on stage server:

    ```text
    POST https://stage.payment.schibsted.no/oauth/token
        client_id=4f6b7595efd04b2d5000000d
        client_secret=foobar
        redirect_uri=fvnereader%3A%2F%2Flogin
        grant_type=authorization_code
        code=55d4f8e7fbd1bd4f3e0a312c7765cecfce37c7ec
        scope=
        state=
    ```

4. Parse the response and retrieve the access token to be used with API calls

    JSON response:

    ```js
    {
        "access_token": "de60c9b77cb2dfaec651ee8af0b55a27e483b172",
        "expires_in": 3600,
        "scope": "",
        "user_id": "198962",
        "is_admin": false,
        "refresh_token": "4bd5eb8d539b692dc696efab10341839a16c6f4b",
        "server_time": 1333537247
    }
    ```

    The [refresh token](http://tools.ietf.org/html/draft-ietf-oauth-v2-10#section-4.1.4)
    can be used to request a new access token when the current one expires.

5. Make REST API calls with the oauth token.

    Example on stage server:

    ```text
    GET https://stage.payment.schibsted.no/api/2/me?oauth_token=de60c9b77cb2dfaec651ee8af0b55a27e483b172
    ```

## Logging the user out of SPiD (and your app)

You can programmatically log the user out of SPiD by redirecting the user to:

```text
https://stage.payment.schibsted.no/logout?redirect_uri=YOUR_REDIRECT_URL&oauth_token=ACCESS_TOKEN
```

`redirect_uri` is optional, and tells SPiD where to redirect the user after
being logged out. If omitted, the user will be redirected to the login page
after being logged out.

`oauth_token` is optional as well. If provided, this particular access token
will be deleted and cannot be used to make further requests.
