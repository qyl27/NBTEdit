package cx.rain.mc.nbtedit.gui.editor;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.editor.EditorButton;
import cx.rain.mc.nbtedit.gui.component.ButtonComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (isHover(mouseX, mouseY)) {
            graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80ffffff);
        }

        graphics.blitSprite(RenderType::guiTextured, button.getSprite(), getX(), getY(), getWidth(), getHeight());

        if (!isActive()) {
            graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
        }
    }
}
