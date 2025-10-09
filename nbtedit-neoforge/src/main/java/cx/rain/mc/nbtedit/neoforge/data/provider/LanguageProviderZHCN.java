package cx.rain.mc.nbtedit.neoforge.data.provider;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LanguageProviderZHCN extends LanguageProvider {
    public LanguageProviderZHCN(PackOutput packOutput) {
        super(packOutput, NBTEdit.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(ModConstants.KEY_CATEGORY, "游戏内NBT编辑器（重制版）");
        add(ModConstants.KEY_NBTEDIT_SHORTCUT, "打开编辑器");

        add(ModConstants.MESSAGE_NOT_PLAYER, "只有在游戏中的玩家可以使用这个命令");
        add(ModConstants.MESSAGE_NO_PERMISSION, "你没有权限使用NBTEdit");
        add(ModConstants.MESSAGE_NOT_LOADED, "方块位置还未加载");
        add(ModConstants.MESSAGE_NOTHING_TO_EDIT, "没有任何目标");
        add(ModConstants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY, "目标方块没有方块实体");
        add(ModConstants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER, "配置文件设置不允许编辑其他玩家");
        add(ModConstants.MESSAGE_UNKNOWN_ENTITY_ID, "实体ID无效");
        add(ModConstants.MESSAGE_EDITING_ENTITY, "编辑实体（ID：%1$s）");
        add(ModConstants.MESSAGE_EDITING_BLOCK_ENTITY, "编辑方块实体（X：%1$s，Y：%2$s，Z：%3$s）");
        add(ModConstants.MESSAGE_EDITING_ITEM_STACK, "编辑物品（%1$s）");
        add(ModConstants.MESSAGE_SAVING_SUCCESSFUL, "保存成功");
        add(ModConstants.MESSAGE_SAVING_FAILED_INVALID_NBT, "保存失败！NBT结构无效");
        add(ModConstants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS, "保存失败！目标方块实体已经不存在了");
        add(ModConstants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS, "保存失败！目标实体已经不存在了");
        add(ModConstants.MESSAGE_MISSING_CLIENT_MOD, "客户端未安装NBTEdit，功能不可用");
        add(ModConstants.MESSAGE_MISSING_SERVER_MOD, "服务器未安装NBTEdit，功能不可用");

        add(ModConstants.NBT_TYPE_BYTE, "字节");
        add(ModConstants.NBT_TYPE_SHORT, "短整数");
        add(ModConstants.NBT_TYPE_INT, "整数");
        add(ModConstants.NBT_TYPE_LONG, "长整数");
        add(ModConstants.NBT_TYPE_FLOAT, "浮点数");
        add(ModConstants.NBT_TYPE_DOUBLE, "双精度浮点数");
        add(ModConstants.NBT_TYPE_BYTE_ARRAY, "字节数组");
        add(ModConstants.NBT_TYPE_STRING, "字符串");
        add(ModConstants.NBT_TYPE_LIST, "列表");
        add(ModConstants.NBT_TYPE_COMPOUND, "组合");
        add(ModConstants.NBT_TYPE_INT_ARRAY, "整数数组");
        add(ModConstants.NBT_TYPE_LONG_ARRAY, "长整数数组");

        add(ModConstants.GUI_TITLE_EDITOR_READ_ONLY, "（只读）");
        add(ModConstants.GUI_TITLE_EDITOR_ENTITY, "编辑实体（ID：%1$s）");
        add(ModConstants.GUI_TITLE_EDITOR_BLOCK_ENTITY, "编辑方块实体（X：%1$s，Y：%2$s，Z：%3$s）");
        add(ModConstants.GUI_TITLE_EDITOR_ITEM_STACK, "编辑物品（%1$s）");
        add(ModConstants.GUI_TITLE_EDITOR_ENTITY_NARRATION, "编辑实体");
        add(ModConstants.GUI_TITLE_EDITOR_BLOCK_ENTITY_NARRATION, "编辑方块实体");
        add(ModConstants.GUI_TITLE_EDITOR_ITEM_STACK_NARRATION, "编辑物品");

        add(ModConstants.GUI_TITLE_TREE_VIEW, "NBT树");
        add(ModConstants.GUI_TITLE_TREE_VIEW_NODE, "NBT节点");
        add(ModConstants.GUI_TITLE_TREE_VIEW_NARRATION, "NBT节点树");
        add(ModConstants.GUI_TITLE_TREE_VIEW_NODE_NARRATION, "NBT节点：%1$s");
        add(ModConstants.GUI_TITLE_SCROLL_BAR, "滚动条");
        add(ModConstants.GUI_TITLE_SCROLL_BAR_NARRATION, "按住拖动");

        add(ModConstants.GUI_TITLE_EDITING_WINDOW, "编辑NBT标签");
        add(ModConstants.GUI_TITLE_EDITING_WINDOW_NARRATION, "NBT标签编辑窗口");
        add(ModConstants.GUI_LABEL_NAME, "名称");
        add(ModConstants.GUI_LABEL_VALUE, "数值");
        add(ModConstants.GUI_EDIT_BOX_NAME, "名称");
        add(ModConstants.GUI_EDIT_BOX_VALUE, "数值");

        add(ModConstants.GUI_BUTTON_COPY, "复制");
        add(ModConstants.GUI_BUTTON_PASTE, "粘贴");
        add(ModConstants.GUI_BUTTON_CUT, "剪切");
        add(ModConstants.GUI_BUTTON_EDIT, "编辑");
        add(ModConstants.GUI_BUTTON_DELETE, "删除");
        add(ModConstants.GUI_BUTTON_ADD, "添加%1$s标签");
        add(ModConstants.GUI_BUTTON_SAVE, "保存");
        add(ModConstants.GUI_BUTTON_QUIT, "退出");
        add(ModConstants.GUI_BUTTON_OK, "确定");
        add(ModConstants.GUI_BUTTON_CANCEL, "取消");

        add(ModConstants.GUI_TOOLTIP_BUTTON_COPY, "复制选中的标签");
        add(ModConstants.GUI_TOOLTIP_BUTTON_PASTE, "在选中的容器中粘贴标签");
        add(ModConstants.GUI_TOOLTIP_BUTTON_CUT, "剪切选中的标签");
        add(ModConstants.GUI_TOOLTIP_BUTTON_EDIT, "编辑选中的标签");
        add(ModConstants.GUI_TOOLTIP_BUTTON_DELETE, "删除选中的标签");
        add(ModConstants.GUI_TOOLTIP_BUTTON_ADD, "添加%1$s类型的标签");

        add(ModConstants.GUI_TOOLTIP_BUTTON_SAVE, "保存并退出编辑器");
        add(ModConstants.GUI_TOOLTIP_BUTTON_SAVE_DISABLED, "无法保存，因为此编辑器是只读的");
        add(ModConstants.GUI_TOOLTIP_BUTTON_QUIT, "退出编辑器但不保存");
        add(ModConstants.GUI_TOOLTIP_BUTTON_OK, "确定");
        add(ModConstants.GUI_TOOLTIP_BUTTON_CANCEL, "取消");

        add(ModConstants.GUI_TOOLTIP_PREVIEW_COMPONENT, "【文本预览】");
        add(ModConstants.GUI_TOOLTIP_PREVIEW_COMPONENT_NARRATION, "文本预览：");
        add(ModConstants.GUI_TOOLTIP_PREVIEW_ITEM, "【物品预览】");
        add(ModConstants.GUI_TOOLTIP_PREVIEW_ITEM_NARRATION, "物品预览：");
        add(ModConstants.GUI_TOOLTIP_PREVIEW_UUID, "【UUID预览】");
        add(ModConstants.GUI_TOOLTIP_PREVIEW_UUID_NARRATION, "UUID预览：");
    }
}
