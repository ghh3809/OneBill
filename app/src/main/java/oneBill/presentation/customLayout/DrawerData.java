package oneBill.presentation.customLayout;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 侧滑菜单内的各条目内容
 * 包括图片id、条目名称
 */
public class DrawerData {

    private String name = "";
    private int icon = 0;

    public DrawerData(String _name, int _icon) {
        this.name = _name;
        this.icon = _icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

}
