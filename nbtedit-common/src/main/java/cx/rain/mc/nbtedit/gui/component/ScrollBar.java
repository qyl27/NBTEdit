package cx.rain.mc.nbtedit.gui.component;

import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class ScrollBar extends AbstractComponent {
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller");
    private static final ResourceLocation SCROLLER_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller_background");

    private final int scrollUnit = getMinecraft().font.lineHeight + 2;

    private final boolean horizontal;
    private final IScrollHandler toScroll;
    private final int contentLength;

    /**
     * Offset between scroll-base to the actual viewport start.
     * (In pixels.)
     */
    private int scrollAmount = 0;
    private boolean scrolling = false;

    public ScrollBar(int x, int y, int width, int height, IScrollHandler toScroll, int contentLength) {
        this(x, y, width, height, toScroll, contentLength, false);
    }

    public ScrollBar(int x, int y, int width, int height, IScrollHandler toScroll, int contentLength, boolean horizontal) {
        super(x, y, width, height, Component.translatable(ModConstants.GUI_TITLE_SCROLL_BAR));

        this.horizontal = horizontal;
        this.toScroll = toScroll;
        this.contentLength = contentLength;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isVertical() {
        return !horizontal;
    }

    public int getPrimaryStart() {
        if (isHorizontal()) {
            return getX();
        }

        return getY();
    }

    public int getPrimaryLength() {
        if (isHorizontal()) {
            return getWidth();
        }

        return getHeight();
    }

    /**
     * Get scroll rate (value between 0 ~ 1).
     * @return the scroll rate
     */
    public double getScrollRate() {
        return Mth.clamp(((double) scrollAmount) / (contentLength - getPrimaryLength()), 0, 1);
    }

    /**
     * Set scroll rate (value between 0 ~ 1).
     * @param scrollRate the scroll rate
     */
    public void setScrollRate(double scrollRate) {
        var actual = Mth.clamp(scrollRate, 0, 1);
        var newScrollAmount = (int) (actual * (contentLength - getPrimaryLength()));
        setScrollAmount(newScrollAmount);
    }

    private int getScrollBarLength() {
        return Mth.clamp((int)((float)(getPrimaryLength() * getPrimaryLength()) / (float)contentLength), 32, getPrimaryLength());
    }

    private int getMaxScrollAmount() {
        return Math.max(0, contentLength - getPrimaryLength());
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(int amount) {
        scrollAmount = Mth.clamp(amount, 0, getMaxScrollAmount());
    }

    private void addScrollAmount(int value) {
        setScrollAmount(getScrollAmount() + value);
        toScroll.onScroll(value);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_BACKGROUND_SPRITE, getX(), getY(), getWidth(), getHeight());

        var barLength = this.getScrollBarLength();
        var barOffset = (int) (getScrollRate() * (getPrimaryLength() - barLength));

        if (isHorizontal()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_SPRITE, getX() + barOffset, getY(), barLength, getHeight());
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_SPRITE, getX(), getY() + barOffset, getWidth(), barLength);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(ModConstants.GUI_TITLE_SCROLL_BAR_NARRATION));
    }

    public boolean isScrolling() {
        return scrolling;
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean isDoubleClick) {
        if (event.buttonInfo().isLeft()) {
            scrolling = true;
        }
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        if (event.buttonInfo().isLeft()) {
            scrolling = false;
        }
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        if (isActive() && scrolling) {
            var mousePrimary = isVertical() ? mouseY : mouseX;
            var dragPrimary = isVertical() ? event.y() : event.x();

            if (mousePrimary < (double) getPrimaryStart()) {
                this.addScrollAmount(-scrollUnit);
            } else if (mousePrimary > (double)(getPrimaryStart() + getPrimaryLength())) {
                this.addScrollAmount(scrollUnit);
            } else {
                var d = Mth.clamp(this.getMaxScrollAmount() / (getPrimaryLength() - getScrollBarLength()), 0, 1);
                this.addScrollAmount((int) (dragPrimary * d));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!isVisible()) {
            return false;
        }

        var scrollPrimary = isVertical() ? scrollY : scrollX;
        addScrollAmount((int) (scrollUnit * -scrollPrimary));

        return true;
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (isHoveredOrFocused()) {
            if (event.key() == GLFW.GLFW_KEY_UP) {
                addScrollAmount(-scrollUnit);
                return true;
            } else if (event.key() == GLFW.GLFW_KEY_DOWN) {
                addScrollAmount(scrollUnit);
                return true;
            }
        }

        return super.keyReleased(event);
    }
}
