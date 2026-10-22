# Verification record

Date: 22 October 2026

## Automated matching tests
RecipeMatcherTest.java was compiled against JUnit 4.13.2 and Hamcrest Core 1.3 and run with JUnitCore, independently of the Android Gradle daemon.

Result: OK (8 tests).

Checks: missing ingredient excluded; insufficient quantity excluded; plural/case/whitespace normalisation with kg/g conversion; incompatible mass/volume rejected; compatible stock entries combined; repeated recipe requirements combined; cup/ml conversion; empty recipe rejected.

This result covers the pure Java matching rule only. It is not an Android instrumentation or UI test result.

To repeat in Android Studio: open app/src/test/java/com/techvee/smartpantry/RecipeMatcherTest.java and use Run on the class. Alternatively run the app testDebugUnitTest Gradle task after syncing dependencies.

## Manual device evidence
The student confirmed passing save, edit, delete, strict quantity matching, reopening persistence and settings retention checks on the Pixel 6 emulator. Twelve actual emulator screenshots were supplied for the report. They include the smoothie appearing with one banana and 250 ml milk, disappearing at 200 ml, and the pantry after Milk was deleted.

The images establish visible states; the narrated demonstration should show transitions, a complete CRUD cycle and closing/reopening the app live.

## Known limitations
Common ingredient aliases only; no density-based mass/volume conversion; no count/weight conversion; no automatic stock consumption; expiry dates organise stock and do not certify food safety. Database work runs on the UI thread and should move to an executor for larger collections.
