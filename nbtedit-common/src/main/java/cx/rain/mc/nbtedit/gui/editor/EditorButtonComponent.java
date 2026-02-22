package cx.rain.mc.nbtedit.gui.editor;

import cx.rain.mc.nbtedit.editor.EditorButton;
import cx.rain.mc.nbtedit.gui.component.ButtonComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public class EditorButtonComponent extends ButtonComponent {
    private final EditorButton button;

    public EditorButtonComponent(EditorButton id, int x, int y, Component message, OnPress onPressed) {
        super(x, y, 9, 9, message, onPressed, DEFAULT_NARRATION);

        button = id;
    }

    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            setTooltip(Tooltip.create(getMessage(), createNarrationMessage()));
            setTooltipDelay(Duration.ofMillis(200));
        } else {
            setTooltip(null);
        }
    }

    public boolean isHover(int mouseX, int mouseY) {
        return isActive()
                && mouseX >= getX()
                && mouseY >= getY()
                && mouseX < getX() + getWidth()
                && mouseY < getY() + getHeight();
    }

    @Override
    protected void renderContents(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isHover(mouseX, mouseY)) {
            guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80ffffff);
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, button.getSprite(), getX(), getY(), getWidth(), getHeight());

        if (!isActive()) {
            guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
        }
    }
}
