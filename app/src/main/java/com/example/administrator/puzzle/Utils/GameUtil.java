package com.example.administrator.puzzle.Utils;

import com.example.administrator.puzzle.Bean.ItemBean;
import com.example.administrator.puzzle.PuzzleMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Android shnu
 * Created by 140153815cyk on 2017/5/23.
 */

public class GameUtil {
    // 游戏信息单元格Bean
    public static List<ItemBean> itemBeans = new ArrayList<ItemBean>();
    // 空格单元格
    public static ItemBean blankItemBean = new ItemBean();

    /**
     * 判断点击的Item是否可移动
     *
     * @param position
     * @return 能否移动
     */
    public static boolean isMoveable(int position) {
        int type = PuzzleMain.type;
        // 获取空格Item
        int blankId = GameUtil.blankItemBean.getItemId() - 1;
        // 不同行 相差为type
        if (Math.abs(blankId - position) == type) {
            return true;
        }
        // 相同行 相差为1
        if ((blankId / type == position / type) && Math.abs(blankId - position) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 交换空格与点击Item的位置
     *
     * @param from
     * @param blank
     */
    public static void swapItems(ItemBean from, ItemBean blank) {
        ItemBean tempItemBean = new ItemBean();
        // 交换BitmapId
        tempItemBean.setBitmapId(from.getBitmapId());
        from.setBitmapId(blank.getBitmapId());
        blank.setBitmapId(tempItemBean.getBitmapId());
        // 交换Bitmap
        tempItemBean.setBitmap(from.getBitmap());
        from.setBitmap(blank.getBitmap());
        blank.setBitmap(tempItemBean.getBitmap());
        // 设置新的Blank
        GameUtil.blankItemBean = from;
    }

    /**
     * 生成随机的Item
     */
    public static void getPuzzleGenerator() {
        int index = 0;
        for (int i = 0; i < itemBeans.size(); i++) {
            //产生[0,1)*type*type的随机整数
            index = (int) (Math.random() * PuzzleMain.type * PuzzleMain.type);
            //list中指针0-8 对应ItemID为1-9,ItemID不会被交换
            swapItems(itemBeans.get(index), GameUtil.blankItemBean);
        }
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < itemBeans.size(); i++) {
            data.add(itemBeans.get(i).getBitmapId());
        }
        // 判断生成是否有解
        if (canSolve(data)) {
            return;
        } else {
            getPuzzleGenerator();
        }
    }

    /**
     * 是否拼图成功
     *
     * @return 是否拼图成功
     */
    public static boolean isSuccess() {
        for (ItemBean tempBean : GameUtil.itemBeans) {
            //ItemID不会被交换，所以只要判断N轮交换过后ItemID是否等于BitmapID即可
            if (tempBean.getBitmapId() != 0 && (tempBean.getItemId()) == tempBean.getBitmapId()) {
                continue;
            } else if (tempBean.getBitmapId() == 0 && tempBean.getItemId() == PuzzleMain.type * PuzzleMain.type) {//空白块判定
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 该数据是否有解
     *
     * @param data
     * @return 该数据是否有解
     */
    public static boolean canSolve(List<Integer> data) {
        // 获取空格Id
        int blankId = GameUtil.blankItemBean.getItemId();
        // 可行性原则
        if (data.size() % 2 == 1) {
            return getInversions(data) % 2 == 0;
        } else {
            // 从底往上数,空格位于奇数行
            if (((int) (blankId - 1) / PuzzleMain.type) % 2 == 1) {
                return getInversions(data) % 2 == 0;
            } else {
                // 从底往上数,空位位于偶数行
                return getInversions(data) % 2 == 1;
            }
        }
    }

    /**
     * 计算倒置和算法
     *
     * @param data
     * @return 该序列的倒置和
     */
    public static int getInversions(List<Integer> data) {
        int inversions = 0;
        int inversionCount = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                int index = data.get(i);
                if (data.get(j) != 0 && data.get(j) < index) {
                    inversionCount++;
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }

}
