# light-sdk
or: a tool for building Tools

## tl;dr
This repository contains the scaffolding for building simple tools for the Light Phone III. Included are a library ([sdk](./sdk)) and placeholder application ([tool](./tool)) that depends on it. To create a tool that is fully compatible with LightOS, you must write your application code within the `tool` module, using the primitives provided by the `sdk`.

You can and should use current Android best practices: Kotlin for all source code, Compose for UI, Coroutines for async programming, and MVVM architecture. **Although this is appears to be a fairly standard Android dev environment, you will quickly find out that we are (gently but broadly) restricting which Android APIs and third-party libraries can be used. This is in an effort to provide a secure and distinctly _light_ experience for our users. More on this later.**

## Quickstart
### Emulator
While you can develop your tool an any Android device with ADB enabled, we recommend creating an emulator with the same properties as the Light Phone III:
* 1080 X 1240, 3.92" display
* Android API 34
* NO Google Play Services installed

### Start Building
Edit the code in `HomeScreen` and `HomeScreenViewModel` to get started. `Homescreen` surfaces a `@Composable` method named `Content`. This is the UI that is shown when the tool first boots. You'll notice this UI sources data from it's `viewModel` field, which is an instance of `HomeScreenViewModel`. Edit that class with your screen's logic and expose the data to the UI using either Compose `State` or Coroutine `Flow`s. If you want to create a new screen, create a new Screen/ViewModel pair: your screen should extend from `LightScreen` and your VM from `LightScreenViewModel`. Your screen implementation will need:
1. A direct reference to your ViewModel's class type
2. A factory method for creating a new instance of your ViewModel.

Look at `HomeScreen` as an example for how this is done. To navigate to your new screen, use the `navigateTo` function built into `LightScreen` - just pass it a lambda to create an instance of your new screen. Note that the `LightScreen` constructor takes in a `SealedLightActivity`. The lambda is provided an instance of this as a default parameter.

Since LightOS does not use Android system navigation, we provide a back button for you. As long as you use `navigateTo` to move between screens, our back button should work great. If need be, you can override `shouldShowBackButton` in your `LightScreen` and/or the `onBackPressed` method in your `LightViewModel`.

