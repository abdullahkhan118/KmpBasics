import SwiftUI
import Shared


@main
struct iOSApp: App {

    init() {
        InitKoin.shared.doInitKoin()

    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
