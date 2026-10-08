package cx.rain.mc.nbtedit.gui.window;

import cx.rain.mc.nbtedit.gui.component.IComposedComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWindowHolder extends IComposedComponent {
    List<IWindow> getWindows();

    void addWindow(IWindow window, boolean mutex, boolean show);

    void closeWindow(IWindow window);

    void show(IWindow window);

    void hide(IWindow window);

    @Nullable
    IWindow getMutexWindow();

    void setMutexWindow(@Nullable IWindow window);

    @Nullable
    IWindow getFocusedWindow();

    void setFocusedWindow(@Nullable IWindow window);

    default void addWindow(IWindow window) {
        addWindow(window, false, true);
    }

    default boolean isFocused(IWindow window) {
        return getFocusedWindow() == window;
    }

    default boolean isWindowMutex(IWindow window) {
        return getMutexWindow() == window;
    }

    default boolean hasMutexWindow() {
        return getMutexWindow() != null;
    }

    default boolean hasWindow() {
        return !getWindows().isEmpty();
    }

    default boolean hasWindow(IWindow window) {
        return getWindows().contains(window);
    }

    default void closeWindows() {
        for (var window : getWindows()) {
            closeWindow(window);
        }
    }
}
