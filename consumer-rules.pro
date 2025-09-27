# Consumer ProGuard rules for Choco library
# These rules will be applied to apps that use the Choco library

# Keep all public API classes and methods
-keep public class com.akustom15.choco.Choco {
    public *;
}

-keep public class com.akustom15.choco.models.** {
    public *;
}

-keep public class com.akustom15.choco.ui.** {
    public *;
}

-keep public class com.akustom15.choco.utils.ChocoConstants {
    public *;
}

-keep public class com.akustom15.choco.utils.ChocoLogger {
    public *;
}

# Keep content providers
-keep class com.akustom15.choco.providers.** { *; }

# Keep model classes for JSON parsing
-keep class com.akustom15.choco.models.PresetInfo { *; }
-keep class com.akustom15.choco.models.PresetData { *; }
