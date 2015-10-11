
import except.GenerateException;



public class NativeManager
{

    static
    {
        try
        {
            System.loadLibrary("NativeManager");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public long nativeAccessPointer;

    // Pointer to access the native zone(int on 32 bit, long on 64 bit)
    public long getNativeAccessPointer()
    {
        return nativeAccessPointer;
    }

    public enum ParameterType
    {
        STRING_TYPE, INT_TYPE, LIST_TYPE, BOOLEAN_TYPE, REAL_TYPE
    };

    public NativeManager()
    {
        create();
    }

    public void finalize()
    {
        destroy();
    }


    public native boolean hasValue(String parameterName);

    public native String getStringValue(String parameterName);

    public native int getIntValue(String parameterName);

    public native float getFloatValue(String parameterName);

    public native boolean getBooleanValue(String parameterName);

    public native String[] getListValue(String parameterName);

    private native void manage(String parameterName, byte parameterType,
                               boolean required);

    public native boolean parseFrom(String filename) throws GenerateException;

    public void manage(String parameterName, ParameterType parameterType,
                       boolean required)
    {
        byte type = '\0';
        switch (parameterType)
        {
        case STRING_TYPE:
            type = 's';
            break;
        case INT_TYPE:
            type = 'i';
            break;
        case LIST_TYPE:
            type = 'l';
            break;
        case BOOLEAN_TYPE:
            type = 'b';
            break;
        case REAL_TYPE:
            type = 'r';
            break;
        }
        manage(parameterName, type, required);
    }
    // Also manage the native heap allocated memory lifecycle
    private native void create();

    private native void destroy();
}
