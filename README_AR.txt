Reverse Talk - مشروع Android

هذا مشروع Android Studio أولي لتطبيق عاكس الكلام.
المزايا:
- عكس النص المكتوب.
- تحويل الكلام إلى نص عبر خدمة SpeechRecognizer في Android.
- اختيار العربية الجزائرية أو العربية أو الفرنسية أو الإنجليزية.
- نسخ النتيجة ومسحها.

إنشاء APK:
1) ثبّت Android Studio على حاسوب.
2) افتح مجلد ReverseTalk الذي يحتوي settings.gradle.
3) انتظر مزامنة Gradle وتنزيل Android SDK المطلوب.
4) من القائمة Build اختر Build Bundle(s) / APK(s) ثم Build APK(s).
5) سيظهر APK داخل app/build/outputs/apk/debug/.

ملاحظة مهمة: لم يتم إرفاق APK جاهز؛ بيئة الإنشاء الحالية لا تحتوي Android SDK أو Gradle.
يلزم Android Studio لتنزيل الأدوات وبناء APK. التعرّف على الصوت يعتمد على خدمة التعرف المثبتة على الهاتف وقد يحتاج إنترنت.

البناء السحابي من الهاتف عبر GitHub Actions:
- ارفع محتويات هذا المجلد إلى مستودع GitHub جديد باسم ReverseTalk.
- تأكد أن الملف .github/workflows/build-apk.yml موجود في المستودع.
- افتح Actions ثم Build Reverse Talk APK ثم Run workflow.
- بعد نجاح البناء افتح نتيجة التشغيل ونزّل Artifact باسم Reverse-Talk-APK.
