# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/iguest/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Basic ProGuard rules for Firebase Android SDK 2.0.0+

# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)

    -keep class com.fasterxml.jackson.databind.ObjectMapper {
        public <methods>;
        protected <methods>;
    }
    -keep class com.fasterxml.jackson.databind.ObjectWriter {
        public ** writeValueAsString(**);
    }

-keepnames class edu.uw.flowww.** { *; }
-keepnames class edu.uw.jpollock.flowww.** { *; }
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**