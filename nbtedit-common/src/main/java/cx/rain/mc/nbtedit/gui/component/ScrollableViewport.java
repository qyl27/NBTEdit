package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ScrollableViewport extends AbstractComposedComponent {

    private final int scrollBarWidth;

    private ScrollBar verticalScrollBar = null;
    private ScrollBar horizontalScrollBar = null;

    private int contentWidth = 0;
    private int contentHeight = 0;

    public ScrollableViewport(int x, int y, int width, int height) {
        this(x, y, width, height, AbstractScrollArea.SCROLLBAR_WIDTH);
    }

    public ScrollableViewport(int x, int y, int width, int height, int scrollBarWidth) {
        super(x, y, width, height, Component.empty());

        this.scrollBarWidth = scrollBarWidth;
    }

    @Override
    public void update() {
        super.update();
        createChildren();
    }

    @Override
    protected void createChildren() {
        contentWidth = 0;
        contentHeight = 0;

        for (var c : getChildren()) {
            var cw = c.getX() + c.getWidth();
            var ch = c.getY() + c.getHeight();
            if (contentWidth < cw) {
                contentWidth = cw;
            }

            if (contentHeight < ch) {
                contentHeight = ch;
            }
        }

        if (contentHeight > getHeight()) {
            var amount = verticalScrollBar != null ? verticalScrollBar.getScrollAmount() : 0;
            verticalScrollBar = new ScrollBar(getX() + getWidth() - getScrollBarWidth(), getY(),
                    getScrollBarWidth(), getHeight(),
                    d -> {}, contentHeight);
            verticalScrollBar.setScrollAmount(amount);
        }

        if (contentWidth > getWidth()) {
            var amount = horizontalScrollBar != null ? horizontalScrollBar.getScrollAmount() : 0;
            horizontalScrollBar = new ScrollBar(getX(), getY() + getHeight() - getScrollBarWidth(),
                    getWidth() - (shouldShowVerticalBar() ? getScrollBarWidth() : 0), getScrollBarWidth(),
                    d -> {}, contentWidth, true);
            horizontalScrollBar.setScrollAmount(amount);
        }
    }

    public boolean shouldShowVerticalBar() {
        return contentHeight > getHeight() && verticalScrollBar != null;
    }

    public boolean shouldShowHorizontalBar() {
        return contentWidth > getWidth() && horizontalScrollBar != null;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var maxX = getX() + getWidth() - (shouldShowVerticalBar() ? getScrollBarWidth() : 0);
        var maxY = getY() + getHeight() - (shouldShowHorizontalBar() ? getScrollBarWidth() : 0);
        guiGraphics.enableScissor(getX(), getY(), maxX, maxY);
        guiGraphics.pose().pushMatrix();

        for (var c : getChildren()) {
            c.setX(c.getX() - getScrollXOffset());
            c.setY(c.getY() - getScrollYOffset());
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.pose().popMatrix();
        guiGraphics.disableScissor();

        if (shouldShowVerticalBar()) {
            verticalScrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        if (shouldShowHorizontalBar()) {
            horizontalScrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public int getScrollBarWidth() {
        return scrollBarWidth;
    }

    public int getScrollXOffset() {
        return shouldShowHorizontalBar() ? horizontalScrollBar.getScrollAmount() : 0;
    }

    public void setScrollXOffset(int value) {
        if (shouldShowHorizontalBar()) {
            horizontalScrollBar.setScrollAmount(value);
        }
    }

    public int getScrollYOffset() {
        return shouldShowVerticalBar() ? verticalScrollBar.getScrollAmount() : 0;
    }

    public void setScrollYOffset(int value) {
        if (shouldShowVerticalBar()) {
            verticalScrollBar.setScrollAmount(value);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        var mouseX = event.x();
        var mouseY = event.y();

        if (shouldShowVerticalBar() && verticalScrollBar.isMouseOver(mouseX, mouseY)) {
            return verticalScrollBar.mouseClicked(event, isDoubleClick);
        }

        if (shouldShowHorizontalBar() && horizontalScrollBar.isMouseOver(mouseX, mouseY)) {
            return horizontalScrollBar.mouseClicked(event, isDoubleClick);
        }

        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        var mouseX = event.x();
        var mouseY = event.y();

        if (shouldShowVerticalBar() && verticalScrollBar.isMouseOver(mouseX, mouseY)) {
            return verticalScrollBar.mouseReleased(event);
        }

        if (shouldShowHorizontalBar() && horizontalScrollBar.isMouseOver(mouseX, mouseY)) {
            return horizontalScrollBar.mouseReleased(event);
        }

        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double nowMouseX, double nowMouseY) {
        var mouseX = event.x();
        var mouseY = event.y();
        var deltaX = mouseX - event.x();
        var deltaY = mouseY - event.y();

        if (shouldShowVerticalBar() && verticalScrollBar.isScrolling() && deltaY != 0) {
            return verticalScrollBar.mouseDragged(event, nowMouseX, nowMouseY);
        }

        if (shouldShowHorizontalBar() && horizontalScrollBar.isScrolling() && deltaX != 0) {
            return horizontalScrollBar.mouseDragged(event, nowMouseX, nowMouseY);
        }

        return super.mouseDragged(event, nowMouseX, nowMouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (shouldShowVerticalBar() && scrollY != 0) {
            return verticalScrollBar.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        if (shouldShowHorizontalBar() && scrollX != 0) {
            return horizontalScrollBar.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        return super.mouseScrolled(mouseX - getX() + getScrollXOffset(), mouseY - getY() + getScrollYOffset(), scrollX, scrollY);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (shouldShowVerticalBar() && verticalScrollBar.isHoveredOrFocused()) {
            return verticalScrollBar.keyReleased(event);
        }

        if (shouldShowHorizontalBar() && horizontalScrollBar.isHoveredOrFocused()) {
            return horizontalScrollBar.keyReleased(event);
        }

        return super.keyReleased(event);
    }
}
