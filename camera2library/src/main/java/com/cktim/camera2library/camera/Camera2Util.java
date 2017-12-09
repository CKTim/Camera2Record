package com.cktim.camera2library.camera;

import android.os.Environment;
import android.util.Size;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cxk on 2017/12/8.
 * <p>
 * 这里为了方便，将部分方法封装到这个Util里面
 */

public class Camera2Util {
    //选择合适的视频size，并且不能大于1080p
    private Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        return choices[choices.length - 1];
    }

    //选择sizeMap中大于并且最接近width和height的size
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }


    // 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择，activity我们已经固定了方向，所以这里无需在做判断
    protected static Size getCloselyPreSize(Size[] sizeMap, int surfaceWidth, int surfaceHeight) {
        int ReqTmpWidth;
        int ReqTmpHeight;
        ReqTmpWidth = surfaceHeight;
        ReqTmpHeight = surfaceWidth;
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Size size : sizeMap) {
            if ((size.getWidth() == ReqTmpWidth) && (size.getHeight() == ReqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Size retSize = null;
        for (Size size : sizeMap) {
            curRatio = ((float) size.getWidth()) / size.getHeight();
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        return retSize;
    }


    /**
     * 核心方法，这里是通过从sizeMap中获取和Textureview宽高比例相同的map，然后在获取接近自己想获取到的尺寸
     * 之所以这么做是因为我们要确保预览尺寸不要太大，这样才不会太卡
     *
     * @param sizeMap
     * @param surfaceWidth
     * @param surfaceHeight
     * @param maxHeight
     * @return
     */
    public static Size getMinPreSize(Size[] sizeMap, int surfaceWidth, int surfaceHeight, int maxHeight) {
        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) surfaceWidth) / surfaceHeight;
        float curRatio;
        List<Size> sizeList = new ArrayList<>();
        Size retSize = null;
        for (Size size : sizeMap) {
            curRatio = ((float) size.getHeight()) / size.getWidth();
            if (reqRatio == curRatio) {
                sizeList.add(size);
            }
        }

        if (sizeList != null && sizeList.size() != 0) {
            for (int i = sizeList.size() - 1; i >= 0; i--) {
                //取Size宽度大于1000的第一个数,这里我们获取大于maxHeight的第一个数，理论上我们是想获取size.getWidth宽度为1080或者1280那些，因为这样的预览尺寸已经足够了
                if (sizeList.get(i).getWidth() >= maxHeight) {
                    retSize = sizeList.get(i);
                    break;
                }
            }

            //可能没有宽度大于maxHeight的size,则取相同比例中最小的那个size
            if (retSize == null) {
                retSize = sizeList.get(sizeList.size() - 1);
            }

        } else {
            retSize = getCloselyPreSize(sizeMap, surfaceWidth, surfaceHeight);
        }
        return retSize;
    }


    /**
     * 使用Camera2录制和所拍的照片都会在这里
     */
    public static String getCamera2Path() {
        String picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraV2/";
        File file = new File(picturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picturePath;
    }

    /**
     * 判断传入的地址是否已经有这个文件夹，没有的话需要创建
     */
    public static void createSavePath(String path){
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
