#include <jni.h>
#include <string>
#include <fcntl.h>
#include <unistd.h>
// kvssd operation
#define CMD_PUT             3
#define CMD_GET             4
#define CMD_ERASE           5
#define CMD_EXIST           6

#define sys_segment     323
#define sys_led_on      324
#define sys_led_off     325
#define sys_kvssd       326

struct kvssd_env {
    uint32_t key[4];
    uint32_t value[1024];

};

extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_MainActivity_display_17segment(JNIEnv *env, jobject thiz,
                                                           jint value) {
    syscall(sys_segment,value);
}
//          LED
extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_MainActivity_led_1off(JNIEnv *env, jobject thiz, jint position) {
    syscall(sys_led_off,position);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_MainActivity_led_1on(JNIEnv *env, jobject thiz, jint position) {
    syscall(sys_led_on,position);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_memo_1main_led_1on(JNIEnv *env, jobject thiz, jint position) {
    // TODO: implement led_on()
    syscall(sys_led_on,position);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_memo_1main_led_1off(JNIEnv *env, jobject thiz, jint position) {
    // TODO: implement led_off()
    syscall(sys_led_off,position);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_MainActivity_kvssd_1op(JNIEnv *env, jobject thiz, jint op_num, jint key,
                                                   jstring value) {
    int ret;
    const char *str;
    struct kvssd_env env1;
    memset(env1.key, 0x00, sizeof(uint32_t) * 4);
    memset(env1.value, 0x00, sizeof(uint32_t) * 1024);

    env1.key[0] = key;
    if (op_num == CMD_PUT) {
        str = (env)->GetStringUTFChars(value, 0);

        for (int i = 0; i < strlen(str); i++) {
            env1.value[i] = str[i];
        }
    }
    syscall(sys_kvssd,op_num,&env1);

}extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_memoapp_11_MainActivity_get_1key_1kvssd(JNIEnv *env, jobject thiz, jint key) {
    int i;
    struct kvssd_env env1;
    unsigned char str[1024];
    memset(env1.key,0x00,sizeof(uint32_t)*4);
    memset(env1.value,0x00,sizeof(uint32_t)*1024);

    env1.key[0] = key;
    syscall(sys_kvssd,CMD_GET,&env1);
    for(i=0; env1.value[1] !=0; i++){
        str[i] = env1.value[i];
    }
    str[i] = '\0';
    return env->NewStringUTF(reinterpret_cast<const char *>(str));
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_memoapp_11_memo_1main_display_17segment(JNIEnv *env, jobject thiz, jint value) {
    syscall(sys_segment,value);
}