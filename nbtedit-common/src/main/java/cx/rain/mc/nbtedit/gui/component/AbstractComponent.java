package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractComponent extends AbstractWidget implements IComponent {

    @Nullable
    private IComposedComponent parent;

    public AbstractComponent(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    protected final Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public @Nullable IComposedComponent getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable IComposedComponent parent) {
        this.parent = parent;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isDisabled() {
        return !active;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.active = !disabled;
    }
}
