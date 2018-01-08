package com.ztd.yyb.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveCache {
    private static boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 保存容器
     *
     * @param <T>
     */
    public static <T> void saveListFile(Context ac, ArrayList<T> infos,
                                        String filename) {
//		LogUtil.e("saveListFile","");
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (ExistSDCard()) {
//				LogUtil.e("saveListFile","ExistSDCard()");
                // 存入数据
                filename = Constants.DATA_FILES + filename;
                File file = new File(filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
            } else {
                fileOutputStream =
                        ac.openFileOutput(filename, Context.MODE_PRIVATE);
            }
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(infos);
//			LogUtil.e("saveListFile", "objectOutputStream.writeObject(infos)");
        } catch (Exception e) {
//			LogUtil.e("saveListFile",""+e.getMessage());
            // TODO: handle exception
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> void saveListFile2(Context ac, ArrayList<T> infos,
                                         String filename) {

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {

            // 存入数据
            filename = Constants.DATA_FILES + filename;
            File file = new File(filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(infos);

        } catch (Exception e) {
            LogUtil.e("saveListFile2", "Exception:" + e.getMessage());
            // TODO: handle exception
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getListFile2(Context ac, ArrayList<T> infos,
                                                String filename) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            // 存入数据
            filename = Constants.DATA_FILES + filename;
            File file = new File(filename);
            // Log.e("geterializable", "filename=" + filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            infos = (ArrayList<T>) objectInputStream.readObject();
            return (ArrayList<T>) infos;

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    // private void toBeSize(Context ac) {
    // File f = ac.getFilesDir();
    // double fileOrFilesSize =
    // FileSizeUtil.getFileOrFilesSize(f,FileSizeUtil.SIZETYPE_MB);
    // if(fileOrFilesSize>1)
    // {
    // }
    // }

    /**
     * 取出容器
     *
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getListFile(Context ac, ArrayList<T> infos,
                                               String filename) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            if (ExistSDCard()) {
                // 存入数据
                filename = Constants.DATA_FILES + filename;
                File file = new File(filename);
                // Log.e("geterializable", "filename=" + filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }
                fileInputStream = new FileInputStream(file.toString());
            } else {
                fileInputStream = ac.openFileInput(filename);
            }
            objectInputStream = new ObjectInputStream(fileInputStream);
            infos = (ArrayList<T>) objectInputStream.readObject();
            return (ArrayList<T>) infos;

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static synchronized void saveObject(Context ac, Object o, String filename) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (ExistSDCard()) {

                // 存入数据
                filename = Constants.DATA_FILES + filename;
                File file = new File(filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
            } else {
                fileOutputStream =
                        ac.openFileOutput(filename, Context.MODE_PRIVATE);
            }
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 取出容器
     */
    @SuppressWarnings("unchecked")
    public static synchronized  Object getObject(Context ac, Object o, String filename) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            if (ExistSDCard()) {
                // 存入数据
                filename = Constants.DATA_FILES + filename;
                File file = new File(filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }
                // 取出数据
                fileInputStream = new FileInputStream(file);
            } else {
                fileInputStream = ac.openFileInput(filename);
            }
            objectInputStream = new ObjectInputStream(fileInputStream);
            o = objectInputStream.readObject();
            return o;

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取版本
     *
     * @param activity
     * @return
     */
    public static String getCurrnversion(Context activity) {
        PackageManager packageManager = activity.getPackageManager();
        try {
            PackageInfo pi = packageManager.getPackageInfo(activity.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Save Bitmap to a file.保存图片到本地。
     *
     * @param bitmap
     * @return error message if the saving is failed. null if the saving is
     * successful.
     * @throws IOException
     */
    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 从本地获取图片
     *
     * @param pathString 文件路径
     * @return 图片
     */
    public static Bitmap getBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 删除图片
     */
    public static void deleteBitmap(String pathString) {
        try {
            File file = new File(pathString);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
