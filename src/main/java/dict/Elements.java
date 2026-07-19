package dict;

import utils.PropertyReader;

public class Elements {

    public static final String SIGN_IN = "Sign in";
    public static final String PROJECTS = "Projects";
    public static final String CREATE_NEW_PROJECT_BUTTON = "Create new project";
    public static final String CREATE_PROJECT_BUTTON = "Create project";
    public static final String TOKEN = PropertyReader.getProperty("token");
    private final String LOGIN="[name='email']";
    private final String PASSWORD="[name='password']";
    private final String GLOBAL_ERROR_ALERT = "div[role='alert'] span.mF0fFk";
    private final String FIELD_ERROR_MESSAGE = "div[role='alert'] span.mF0fFk";
}
