package com.rsargsyan.metafilm.main_ctx.core.app;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TmdbGenreTagMapping {

  private TmdbGenreTagMapping() {}

  public static final Map<Integer, String> MOVIE_GENRES = Map.ofEntries(
      Map.entry(28, "action"),
      Map.entry(12, "adventure"),
      Map.entry(16, "animation"),
      Map.entry(35, "comedy"),
      Map.entry(80, "crime"),
      Map.entry(99, "documentary"),
      Map.entry(18, "drama"),
      Map.entry(10751, "family"),
      Map.entry(14, "fantasy"),
      Map.entry(36, "history"),
      Map.entry(27, "horror"),
      Map.entry(10402, "music"),
      Map.entry(9648, "mystery"),
      Map.entry(10749, "romance"),
      Map.entry(878, "science-fiction"),
      Map.entry(10770, "tv-movie"),
      Map.entry(53, "thriller"),
      Map.entry(10752, "war"),
      Map.entry(37, "western")
  );

  public static final Map<Integer, String> TV_GENRES = Map.ofEntries(
      Map.entry(10759, "action-adventure"),
      Map.entry(16, "animation"),
      Map.entry(35, "comedy"),
      Map.entry(80, "crime"),
      Map.entry(99, "documentary"),
      Map.entry(18, "drama"),
      Map.entry(10751, "family"),
      Map.entry(14, "fantasy"),
      Map.entry(36, "history"),
      Map.entry(27, "horror"),
      Map.entry(10402, "music"),
      Map.entry(9648, "mystery"),
      Map.entry(10749, "romance"),
      Map.entry(10762, "kids"),
      Map.entry(10763, "news"),
      Map.entry(10764, "reality"),
      Map.entry(10765, "sci-fi-fantasy"),
      Map.entry(10766, "soap"),
      Map.entry(10767, "talk"),
      Map.entry(10768, "war-politics"),
      Map.entry(37, "western")
  );

  public static final Map<String, String> ALL_TAG_NAMES = Map.ofEntries(
      Map.entry("action", "Action"),
      Map.entry("adventure", "Adventure"),
      Map.entry("animation", "Animation"),
      Map.entry("comedy", "Comedy"),
      Map.entry("crime", "Crime"),
      Map.entry("documentary", "Documentary"),
      Map.entry("drama", "Drama"),
      Map.entry("family", "Family"),
      Map.entry("fantasy", "Fantasy"),
      Map.entry("history", "History"),
      Map.entry("horror", "Horror"),
      Map.entry("music", "Music"),
      Map.entry("mystery", "Mystery"),
      Map.entry("romance", "Romance"),
      Map.entry("science-fiction", "Science Fiction"),
      Map.entry("tv-movie", "TV Movie"),
      Map.entry("thriller", "Thriller"),
      Map.entry("war", "War"),
      Map.entry("western", "Western"),
      Map.entry("action-adventure", "Action & Adventure"),
      Map.entry("kids", "Kids"),
      Map.entry("news", "News"),
      Map.entry("reality", "Reality"),
      Map.entry("sci-fi-fantasy", "Sci-Fi & Fantasy"),
      Map.entry("soap", "Soap"),
      Map.entry("talk", "Talk"),
      Map.entry("war-politics", "War & Politics")
  );

  public static final Map<String, Map<String, String>> ALL_TAG_LOCALIZATIONS = buildLocalizations();

  private static Map<String, Map<String, String>> buildLocalizations() {
    Map<String, Map<String, String>> m = new LinkedHashMap<>();

    m.put("action", Map.ofEntries(
        Map.entry("EN", "Action"), Map.entry("EN_US", "Action"), Map.entry("EN_GB", "Action"), Map.entry("EN_AU", "Action"),
        Map.entry("RU", "Боевик"), Map.entry("FR", "Action"), Map.entry("FR_FR", "Action"), Map.entry("FR_CA", "Action"),
        Map.entry("DE", "Action"), Map.entry("ES", "Acción"), Map.entry("ES_ES", "Acción"), Map.entry("ES_419", "Acción"),
        Map.entry("IT", "Azione"), Map.entry("PT", "Ação"), Map.entry("PT_BR", "Ação"), Map.entry("PT_PT", "Ação"),
        Map.entry("JA", "アクション"), Map.entry("KO", "액션"),
        Map.entry("ZH", "动作"), Map.entry("ZH_HANS_CN", "动作"), Map.entry("ZH_HANT_TW", "動作"), Map.entry("ZH_HANT_HK", "動作"),
        Map.entry("HI", "एक्शन"), Map.entry("BN", "অ্যাকশন"), Map.entry("TR", "Aksiyon"),
        Map.entry("EL", "Δράση"), Map.entry("TH", "แอคชั่น"), Map.entry("ID", "Aksi"), Map.entry("MS", "Aksi"),
        Map.entry("VI", "Hành động"), Map.entry("DA", "Action"), Map.entry("SV", "Action"), Map.entry("NB", "Action"),
        Map.entry("NL", "Actie"), Map.entry("NL_NL", "Actie"), Map.entry("NL_BE", "Actie"),
        Map.entry("PL", "Akcja"), Map.entry("UK", "Бойовик"), Map.entry("BE", "Баявік"), Map.entry("BG", "Екшън"),
        Map.entry("CS", "Akční"), Map.entry("SK", "Akčný"), Map.entry("SL", "Akcijski"), Map.entry("HR", "Akcija"),
        Map.entry("SR", "Акција"), Map.entry("MK", "Акција"), Map.entry("HU", "Akció"), Map.entry("RO", "Acțiune"),
        Map.entry("ET", "Märulifilm"), Map.entry("LV", "Asa sižeta"), Map.entry("LT", "Veiksmo"), Map.entry("FI", "Toiminta"),
        Map.entry("HY", "Բոևական"), Map.entry("FA", "اکشن"), Map.entry("AR", "أكشن"), Map.entry("HE", "אקשן"),
        Map.entry("TA", "சாகசம்"), Map.entry("TE", "యాక్షన్"), Map.entry("ML", "ആക്ഷൻ"), Map.entry("UR", "ایکشن"),
        Map.entry("TL", "Aksyon"), Map.entry("BS", "Akcija"), Map.entry("SQ", "Aksion"), Map.entry("AZ", "Döyüş"),
        Map.entry("KA", "სამოქმედო"), Map.entry("MYN", "Akción")
    ));

    m.put("adventure", Map.ofEntries(
        Map.entry("EN", "Adventure"), Map.entry("EN_US", "Adventure"), Map.entry("EN_GB", "Adventure"), Map.entry("EN_AU", "Adventure"),
        Map.entry("RU", "Приключения"), Map.entry("FR", "Aventure"), Map.entry("FR_FR", "Aventure"), Map.entry("FR_CA", "Aventure"),
        Map.entry("DE", "Abenteuer"), Map.entry("ES", "Aventura"), Map.entry("ES_ES", "Aventura"), Map.entry("ES_419", "Aventura"),
        Map.entry("IT", "Avventura"), Map.entry("PT", "Aventura"), Map.entry("PT_BR", "Aventura"), Map.entry("PT_PT", "Aventura"),
        Map.entry("JA", "アドベンチャー"), Map.entry("KO", "어드벤처"),
        Map.entry("ZH", "冒险"), Map.entry("ZH_HANS_CN", "冒险"), Map.entry("ZH_HANT_TW", "冒險"), Map.entry("ZH_HANT_HK", "冒險"),
        Map.entry("HI", "रोमांच"), Map.entry("BN", "অ্যাডভেঞ্চার"), Map.entry("TR", "Macera"),
        Map.entry("EL", "Περιπέτεια"), Map.entry("TH", "การผจญภัย"), Map.entry("ID", "Petualangan"), Map.entry("MS", "Pengembaraan"),
        Map.entry("VI", "Phiêu lưu"), Map.entry("DA", "Eventyr"), Map.entry("SV", "Äventyr"), Map.entry("NB", "Eventyr"),
        Map.entry("NL", "Avontuur"), Map.entry("NL_NL", "Avontuur"), Map.entry("NL_BE", "Avontuur"),
        Map.entry("PL", "Przygoda"), Map.entry("UK", "Пригоди"), Map.entry("BE", "Прыгоды"), Map.entry("BG", "Приключение"),
        Map.entry("CS", "Dobrodružný"), Map.entry("SK", "Dobrodružný"), Map.entry("SL", "Pustolovščina"), Map.entry("HR", "Pustolovine"),
        Map.entry("SR", "Авантура"), Map.entry("MK", "Авантура"), Map.entry("HU", "Kaland"), Map.entry("RO", "Aventură"),
        Map.entry("ET", "Seiklusfilm"), Map.entry("LV", "Piedzīvojumu"), Map.entry("LT", "Nuotykių"), Map.entry("FI", "Seikkailu"),
        Map.entry("HY", "Արկածային"), Map.entry("FA", "ماجرایی"), Map.entry("AR", "مغامرة"), Map.entry("HE", "הרפתקאות"),
        Map.entry("TA", "சாகசப் பயணம்"), Map.entry("TE", "సాహసం"), Map.entry("ML", "സാഹസം"), Map.entry("UR", "مہم جوئی"),
        Map.entry("TL", "Pakikipagsapalaran"), Map.entry("BS", "Pustolovine"), Map.entry("SQ", "Aventurë"), Map.entry("AZ", "Macəra"),
        Map.entry("KA", "სათავგადასავლო"), Map.entry("MYN", "Aventura")
    ));

    m.put("animation", Map.ofEntries(
        Map.entry("EN", "Animation"), Map.entry("EN_US", "Animation"), Map.entry("EN_GB", "Animation"), Map.entry("EN_AU", "Animation"),
        Map.entry("RU", "Анимация"), Map.entry("FR", "Animation"), Map.entry("FR_FR", "Animation"), Map.entry("FR_CA", "Animation"),
        Map.entry("DE", "Animation"), Map.entry("ES", "Animación"), Map.entry("ES_ES", "Animación"), Map.entry("ES_419", "Animación"),
        Map.entry("IT", "Animazione"), Map.entry("PT", "Animação"), Map.entry("PT_BR", "Animação"), Map.entry("PT_PT", "Animação"),
        Map.entry("JA", "アニメーション"), Map.entry("KO", "애니메이션"),
        Map.entry("ZH", "动画"), Map.entry("ZH_HANS_CN", "动画"), Map.entry("ZH_HANT_TW", "動畫"), Map.entry("ZH_HANT_HK", "動畫"),
        Map.entry("HI", "एनिमेशन"), Map.entry("BN", "অ্যানিমেশন"), Map.entry("TR", "Animasyon"),
        Map.entry("EL", "Κινούμενα σχέδια"), Map.entry("TH", "แอนิเมชัน"), Map.entry("ID", "Animasi"), Map.entry("MS", "Animasi"),
        Map.entry("VI", "Hoạt hình"), Map.entry("DA", "Animation"), Map.entry("SV", "Animation"), Map.entry("NB", "Animasjon"),
        Map.entry("NL", "Animatie"), Map.entry("NL_NL", "Animatie"), Map.entry("NL_BE", "Animatie"),
        Map.entry("PL", "Animacja"), Map.entry("UK", "Анімація"), Map.entry("BE", "Анімацыя"), Map.entry("BG", "Анимация"),
        Map.entry("CS", "Animovaný"), Map.entry("SK", "Animovaný"), Map.entry("SL", "Animacija"), Map.entry("HR", "Animacija"),
        Map.entry("SR", "Анимација"), Map.entry("MK", "Анимација"), Map.entry("HU", "Animáció"), Map.entry("RO", "Animație"),
        Map.entry("ET", "Animatsioon"), Map.entry("LV", "Animācija"), Map.entry("LT", "Animacija"), Map.entry("FI", "Animaatio"),
        Map.entry("HY", "Անիմացիա"), Map.entry("FA", "انیمیشن"), Map.entry("AR", "رسوم متحركة"), Map.entry("HE", "אנימציה"),
        Map.entry("TA", "அனிமேஷன்"), Map.entry("TE", "యానిమేషన్"), Map.entry("ML", "ആനിമേഷൻ"), Map.entry("UR", "اینیمیشن"),
        Map.entry("TL", "Animasyon"), Map.entry("BS", "Animacija"), Map.entry("SQ", "Animacion"), Map.entry("AZ", "Animasiya"),
        Map.entry("KA", "ანიმაცია"), Map.entry("MYN", "Animación")
    ));

    m.put("comedy", Map.ofEntries(
        Map.entry("EN", "Comedy"), Map.entry("EN_US", "Comedy"), Map.entry("EN_GB", "Comedy"), Map.entry("EN_AU", "Comedy"),
        Map.entry("RU", "Комедия"), Map.entry("FR", "Comédie"), Map.entry("FR_FR", "Comédie"), Map.entry("FR_CA", "Comédie"),
        Map.entry("DE", "Komödie"), Map.entry("ES", "Comedia"), Map.entry("ES_ES", "Comedia"), Map.entry("ES_419", "Comedia"),
        Map.entry("IT", "Commedia"), Map.entry("PT", "Comédia"), Map.entry("PT_BR", "Comédia"), Map.entry("PT_PT", "Comédia"),
        Map.entry("JA", "コメディ"), Map.entry("KO", "코미디"),
        Map.entry("ZH", "喜剧"), Map.entry("ZH_HANS_CN", "喜剧"), Map.entry("ZH_HANT_TW", "喜劇"), Map.entry("ZH_HANT_HK", "喜劇"),
        Map.entry("HI", "हास्य"), Map.entry("BN", "কমেডি"), Map.entry("TR", "Komedi"),
        Map.entry("EL", "Κωμωδία"), Map.entry("TH", "ตลก"), Map.entry("ID", "Komedi"), Map.entry("MS", "Komedi"),
        Map.entry("VI", "Hài hước"), Map.entry("DA", "Komedie"), Map.entry("SV", "Komedi"), Map.entry("NB", "Komedie"),
        Map.entry("NL", "Komedie"), Map.entry("NL_NL", "Komedie"), Map.entry("NL_BE", "Komedie"),
        Map.entry("PL", "Komedia"), Map.entry("UK", "Комедія"), Map.entry("BE", "Камедыя"), Map.entry("BG", "Комедия"),
        Map.entry("CS", "Komedie"), Map.entry("SK", "Komédia"), Map.entry("SL", "Komedija"), Map.entry("HR", "Komedija"),
        Map.entry("SR", "Комедија"), Map.entry("MK", "Комедија"), Map.entry("HU", "Vígjáték"), Map.entry("RO", "Comedie"),
        Map.entry("ET", "Komöödia"), Map.entry("LV", "Komēdija"), Map.entry("LT", "Komedija"), Map.entry("FI", "Komedia"),
        Map.entry("HY", "Կատակերգություն"), Map.entry("FA", "کمدی"), Map.entry("AR", "كوميدي"), Map.entry("HE", "קומדיה"),
        Map.entry("TA", "நகைச்சுவை"), Map.entry("TE", "కామెడీ"), Map.entry("ML", "കോമഡി"), Map.entry("UR", "کامیڈی"),
        Map.entry("TL", "Komedya"), Map.entry("BS", "Komedija"), Map.entry("SQ", "Komedi"), Map.entry("AZ", "Komediya"),
        Map.entry("KA", "კომედია"), Map.entry("MYN", "Comedia")
    ));

    m.put("crime", Map.ofEntries(
        Map.entry("EN", "Crime"), Map.entry("EN_US", "Crime"), Map.entry("EN_GB", "Crime"), Map.entry("EN_AU", "Crime"),
        Map.entry("RU", "Криминал"), Map.entry("FR", "Crime"), Map.entry("FR_FR", "Crime"), Map.entry("FR_CA", "Crime"),
        Map.entry("DE", "Krimi"), Map.entry("ES", "Crimen"), Map.entry("ES_ES", "Crimen"), Map.entry("ES_419", "Crimen"),
        Map.entry("IT", "Crimine"), Map.entry("PT", "Crime"), Map.entry("PT_BR", "Crime"), Map.entry("PT_PT", "Crime"),
        Map.entry("JA", "犯罪"), Map.entry("KO", "범죄"),
        Map.entry("ZH", "犯罪"), Map.entry("ZH_HANS_CN", "犯罪"), Map.entry("ZH_HANT_TW", "犯罪"), Map.entry("ZH_HANT_HK", "犯罪"),
        Map.entry("HI", "अपराध"), Map.entry("BN", "অপরাধ"), Map.entry("TR", "Suç"),
        Map.entry("EL", "Έγκλημα"), Map.entry("TH", "อาชญากรรม"), Map.entry("ID", "Kriminal"), Map.entry("MS", "Jenayah"),
        Map.entry("VI", "Tội phạm"), Map.entry("DA", "Kriminalitet"), Map.entry("SV", "Brott"), Map.entry("NB", "Kriminalitet"),
        Map.entry("NL", "Misdaad"), Map.entry("NL_NL", "Misdaad"), Map.entry("NL_BE", "Misdaad"),
        Map.entry("PL", "Kryminał"), Map.entry("UK", "Кримінал"), Map.entry("BE", "Крымінал"), Map.entry("BG", "Криминален"),
        Map.entry("CS", "Krimi"), Map.entry("SK", "Krimi"), Map.entry("SL", "Kriminalka"), Map.entry("HR", "Kriminal"),
        Map.entry("SR", "Криминал"), Map.entry("MK", "Криминал"), Map.entry("HU", "Bűnügyi"), Map.entry("RO", "Crimă"),
        Map.entry("ET", "Kriminaalfilm"), Map.entry("LV", "Kriminālfilma"), Map.entry("LT", "Kriminalinis"), Map.entry("FI", "Rikos"),
        Map.entry("HY", "Հանցագործություն"), Map.entry("FA", "جنایی"), Map.entry("AR", "جريمة"), Map.entry("HE", "פשע"),
        Map.entry("TA", "குற்றம்"), Map.entry("TE", "నేరం"), Map.entry("ML", "കുറ്റകൃത്യം"), Map.entry("UR", "جرائم"),
        Map.entry("TL", "Krimen"), Map.entry("BS", "Kriminal"), Map.entry("SQ", "Krim"), Map.entry("AZ", "Cinayət"),
        Map.entry("KA", "კრიმინალი"), Map.entry("MYN", "Crimen")
    ));

    m.put("documentary", Map.ofEntries(
        Map.entry("EN", "Documentary"), Map.entry("EN_US", "Documentary"), Map.entry("EN_GB", "Documentary"), Map.entry("EN_AU", "Documentary"),
        Map.entry("RU", "Документальный"), Map.entry("FR", "Documentaire"), Map.entry("FR_FR", "Documentaire"), Map.entry("FR_CA", "Documentaire"),
        Map.entry("DE", "Dokumentation"), Map.entry("ES", "Documental"), Map.entry("ES_ES", "Documental"), Map.entry("ES_419", "Documental"),
        Map.entry("IT", "Documentario"), Map.entry("PT", "Documentário"), Map.entry("PT_BR", "Documentário"), Map.entry("PT_PT", "Documentário"),
        Map.entry("JA", "ドキュメンタリー"), Map.entry("KO", "다큐멘터리"),
        Map.entry("ZH", "纪录片"), Map.entry("ZH_HANS_CN", "纪录片"), Map.entry("ZH_HANT_TW", "紀錄片"), Map.entry("ZH_HANT_HK", "紀錄片"),
        Map.entry("HI", "डॉक्यूमेंट्री"), Map.entry("BN", "তথ্যচিত্র"), Map.entry("TR", "Belgesel"),
        Map.entry("EL", "Ντοκιμαντέρ"), Map.entry("TH", "สารคดี"), Map.entry("ID", "Dokumenter"), Map.entry("MS", "Dokumentari"),
        Map.entry("VI", "Tài liệu"), Map.entry("DA", "Dokumentar"), Map.entry("SV", "Dokumentär"), Map.entry("NB", "Dokumentar"),
        Map.entry("NL", "Documentaire"), Map.entry("NL_NL", "Documentaire"), Map.entry("NL_BE", "Documentaire"),
        Map.entry("PL", "Dokumentalny"), Map.entry("UK", "Документальний"), Map.entry("BE", "Дакументальны"), Map.entry("BG", "Документален"),
        Map.entry("CS", "Dokumentární"), Map.entry("SK", "Dokumentárny"), Map.entry("SL", "Dokumentarec"), Map.entry("HR", "Dokumentarni"),
        Map.entry("SR", "Документарни"), Map.entry("MK", "Документарен"), Map.entry("HU", "Dokumentumfilm"), Map.entry("RO", "Documentar"),
        Map.entry("ET", "Dokumentaalfilm"), Map.entry("LV", "Dokumentālā"), Map.entry("LT", "Dokumentinis"), Map.entry("FI", "Dokumentti"),
        Map.entry("HY", "Վավերագրական"), Map.entry("FA", "مستند"), Map.entry("AR", "وثائقي"), Map.entry("HE", "דוקומנטרי"),
        Map.entry("TA", "ஆவணப்படம்"), Map.entry("TE", "డాక్యుమెంటరీ"), Map.entry("ML", "ഡോക്യുമെന്ററി"), Map.entry("UR", "دستاویزی"),
        Map.entry("TL", "Dokumentaryo"), Map.entry("BS", "Dokumentarni"), Map.entry("SQ", "Dokumentar"), Map.entry("AZ", "Sənədli"),
        Map.entry("KA", "დოკუმენტური"), Map.entry("MYN", "Documental")
    ));

    m.put("drama", Map.ofEntries(
        Map.entry("EN", "Drama"), Map.entry("EN_US", "Drama"), Map.entry("EN_GB", "Drama"), Map.entry("EN_AU", "Drama"),
        Map.entry("RU", "Драма"), Map.entry("FR", "Drame"), Map.entry("FR_FR", "Drame"), Map.entry("FR_CA", "Drame"),
        Map.entry("DE", "Drama"), Map.entry("ES", "Drama"), Map.entry("ES_ES", "Drama"), Map.entry("ES_419", "Drama"),
        Map.entry("IT", "Dramma"), Map.entry("PT", "Drama"), Map.entry("PT_BR", "Drama"), Map.entry("PT_PT", "Drama"),
        Map.entry("JA", "ドラマ"), Map.entry("KO", "드라마"),
        Map.entry("ZH", "剧情"), Map.entry("ZH_HANS_CN", "剧情"), Map.entry("ZH_HANT_TW", "劇情"), Map.entry("ZH_HANT_HK", "劇情"),
        Map.entry("HI", "नाटक"), Map.entry("BN", "নাটক"), Map.entry("TR", "Drama"),
        Map.entry("EL", "Δράμα"), Map.entry("TH", "ดราม่า"), Map.entry("ID", "Drama"), Map.entry("MS", "Drama"),
        Map.entry("VI", "Kịch tính"), Map.entry("DA", "Drama"), Map.entry("SV", "Drama"), Map.entry("NB", "Drama"),
        Map.entry("NL", "Drama"), Map.entry("NL_NL", "Drama"), Map.entry("NL_BE", "Drama"),
        Map.entry("PL", "Dramat"), Map.entry("UK", "Драма"), Map.entry("BE", "Драма"), Map.entry("BG", "Драма"),
        Map.entry("CS", "Drama"), Map.entry("SK", "Dráma"), Map.entry("SL", "Drama"), Map.entry("HR", "Drama"),
        Map.entry("SR", "Драма"), Map.entry("MK", "Драма"), Map.entry("HU", "Dráma"), Map.entry("RO", "Dramă"),
        Map.entry("ET", "Draama"), Map.entry("LV", "Drāma"), Map.entry("LT", "Drama"), Map.entry("FI", "Draama"),
        Map.entry("HY", "Դրամա"), Map.entry("FA", "درام"), Map.entry("AR", "دراما"), Map.entry("HE", "דרמה"),
        Map.entry("TA", "நாடகம்"), Map.entry("TE", "డ్రామా"), Map.entry("ML", "ഡ്രാമ"), Map.entry("UR", "ڈرامہ"),
        Map.entry("TL", "Drama"), Map.entry("BS", "Drama"), Map.entry("SQ", "Dramë"), Map.entry("AZ", "Dram"),
        Map.entry("KA", "დრამა"), Map.entry("MYN", "Drama")
    ));

    m.put("family", Map.ofEntries(
        Map.entry("EN", "Family"), Map.entry("EN_US", "Family"), Map.entry("EN_GB", "Family"), Map.entry("EN_AU", "Family"),
        Map.entry("RU", "Семейный"), Map.entry("FR", "Famille"), Map.entry("FR_FR", "Famille"), Map.entry("FR_CA", "Famille"),
        Map.entry("DE", "Familie"), Map.entry("ES", "Familia"), Map.entry("ES_ES", "Familia"), Map.entry("ES_419", "Familia"),
        Map.entry("IT", "Famiglia"), Map.entry("PT", "Família"), Map.entry("PT_BR", "Família"), Map.entry("PT_PT", "Família"),
        Map.entry("JA", "ファミリー"), Map.entry("KO", "가족"),
        Map.entry("ZH", "家庭"), Map.entry("ZH_HANS_CN", "家庭"), Map.entry("ZH_HANT_TW", "家庭"), Map.entry("ZH_HANT_HK", "家庭"),
        Map.entry("HI", "परिवार"), Map.entry("BN", "পরিবার"), Map.entry("TR", "Aile"),
        Map.entry("EL", "Οικογένεια"), Map.entry("TH", "ครอบครัว"), Map.entry("ID", "Keluarga"), Map.entry("MS", "Keluarga"),
        Map.entry("VI", "Gia đình"), Map.entry("DA", "Familie"), Map.entry("SV", "Familj"), Map.entry("NB", "Familie"),
        Map.entry("NL", "Familie"), Map.entry("NL_NL", "Familie"), Map.entry("NL_BE", "Familie"),
        Map.entry("PL", "Rodzinny"), Map.entry("UK", "Сімейний"), Map.entry("BE", "Сямейны"), Map.entry("BG", "Семеен"),
        Map.entry("CS", "Rodinný"), Map.entry("SK", "Rodinný"), Map.entry("SL", "Družinski"), Map.entry("HR", "Obiteljski"),
        Map.entry("SR", "Породични"), Map.entry("MK", "Семеен"), Map.entry("HU", "Családi"), Map.entry("RO", "Familie"),
        Map.entry("ET", "Perekond"), Map.entry("LV", "Ģimenes"), Map.entry("LT", "Šeiminis"), Map.entry("FI", "Perhe"),
        Map.entry("HY", "Ընտանեկան"), Map.entry("FA", "خانوادگی"), Map.entry("AR", "عائلي"), Map.entry("HE", "משפחה"),
        Map.entry("TA", "குடும்பம்"), Map.entry("TE", "కుటుంబం"), Map.entry("ML", "കുടുംബം"), Map.entry("UR", "خاندانی"),
        Map.entry("TL", "Pamilya"), Map.entry("BS", "Porodični"), Map.entry("SQ", "Familje"), Map.entry("AZ", "Ailə"),
        Map.entry("KA", "საოჯახო"), Map.entry("MYN", "Familia")
    ));

    m.put("fantasy", Map.ofEntries(
        Map.entry("EN", "Fantasy"), Map.entry("EN_US", "Fantasy"), Map.entry("EN_GB", "Fantasy"), Map.entry("EN_AU", "Fantasy"),
        Map.entry("RU", "Фэнтези"), Map.entry("FR", "Fantaisie"), Map.entry("FR_FR", "Fantaisie"), Map.entry("FR_CA", "Fantaisie"),
        Map.entry("DE", "Fantasy"), Map.entry("ES", "Fantasía"), Map.entry("ES_ES", "Fantasía"), Map.entry("ES_419", "Fantasía"),
        Map.entry("IT", "Fantasy"), Map.entry("PT", "Fantasia"), Map.entry("PT_BR", "Fantasia"), Map.entry("PT_PT", "Fantasia"),
        Map.entry("JA", "ファンタジー"), Map.entry("KO", "판타지"),
        Map.entry("ZH", "奇幻"), Map.entry("ZH_HANS_CN", "奇幻"), Map.entry("ZH_HANT_TW", "奇幻"), Map.entry("ZH_HANT_HK", "奇幻"),
        Map.entry("HI", "फंतासी"), Map.entry("BN", "ফ্যান্টাসি"), Map.entry("TR", "Fantezi"),
        Map.entry("EL", "Φαντασία"), Map.entry("TH", "แฟนตาซี"), Map.entry("ID", "Fantasi"), Map.entry("MS", "Fantasi"),
        Map.entry("VI", "Kỳ ảo"), Map.entry("DA", "Fantasy"), Map.entry("SV", "Fantasy"), Map.entry("NB", "Fantasy"),
        Map.entry("NL", "Fantasy"), Map.entry("NL_NL", "Fantasy"), Map.entry("NL_BE", "Fantasy"),
        Map.entry("PL", "Fantasy"), Map.entry("UK", "Фентезі"), Map.entry("BE", "Фэнтэзі"), Map.entry("BG", "Фентъзи"),
        Map.entry("CS", "Fantasy"), Map.entry("SK", "Fantasy"), Map.entry("SL", "Fantazija"), Map.entry("HR", "Fantazija"),
        Map.entry("SR", "Фантазија"), Map.entry("MK", "Фантазија"), Map.entry("HU", "Fantasy"), Map.entry("RO", "Fantasy"),
        Map.entry("ET", "Fantaasia"), Map.entry("LV", "Fantāzija"), Map.entry("LT", "Fantastika"), Map.entry("FI", "Fantasia"),
        Map.entry("HY", "Ֆանտազիա"), Map.entry("FA", "فانتزی"), Map.entry("AR", "خيال"), Map.entry("HE", "פנטזיה"),
        Map.entry("TA", "கற்பனை"), Map.entry("TE", "కల్పన"), Map.entry("ML", "ഫാന്റസി"), Map.entry("UR", "فنتاسی"),
        Map.entry("TL", "Pantasya"), Map.entry("BS", "Fantazija"), Map.entry("SQ", "Fantazi"), Map.entry("AZ", "Fantaziya"),
        Map.entry("KA", "ფანტასტიკა"), Map.entry("MYN", "Fantasía")
    ));

    m.put("history", Map.ofEntries(
        Map.entry("EN", "History"), Map.entry("EN_US", "History"), Map.entry("EN_GB", "History"), Map.entry("EN_AU", "History"),
        Map.entry("RU", "История"), Map.entry("FR", "Histoire"), Map.entry("FR_FR", "Histoire"), Map.entry("FR_CA", "Histoire"),
        Map.entry("DE", "Geschichte"), Map.entry("ES", "Historia"), Map.entry("ES_ES", "Historia"), Map.entry("ES_419", "Historia"),
        Map.entry("IT", "Storia"), Map.entry("PT", "História"), Map.entry("PT_BR", "História"), Map.entry("PT_PT", "História"),
        Map.entry("JA", "歴史"), Map.entry("KO", "역사"),
        Map.entry("ZH", "历史"), Map.entry("ZH_HANS_CN", "历史"), Map.entry("ZH_HANT_TW", "歷史"), Map.entry("ZH_HANT_HK", "歷史"),
        Map.entry("HI", "इतिहास"), Map.entry("BN", "ইতিহাস"), Map.entry("TR", "Tarih"),
        Map.entry("EL", "Ιστορία"), Map.entry("TH", "ประวัติศาสตร์"), Map.entry("ID", "Sejarah"), Map.entry("MS", "Sejarah"),
        Map.entry("VI", "Lịch sử"), Map.entry("DA", "Historie"), Map.entry("SV", "Historia"), Map.entry("NB", "Historie"),
        Map.entry("NL", "Geschiedenis"), Map.entry("NL_NL", "Geschiedenis"), Map.entry("NL_BE", "Geschiedenis"),
        Map.entry("PL", "Historia"), Map.entry("UK", "Історія"), Map.entry("BE", "Гісторыя"), Map.entry("BG", "История"),
        Map.entry("CS", "Historický"), Map.entry("SK", "Historický"), Map.entry("SL", "Zgodovinski"), Map.entry("HR", "Povijesni"),
        Map.entry("SR", "Историјски"), Map.entry("MK", "Историски"), Map.entry("HU", "Történelmi"), Map.entry("RO", "Istorie"),
        Map.entry("ET", "Ajalooline"), Map.entry("LV", "Vēsturiskā"), Map.entry("LT", "Istorinis"), Map.entry("FI", "Historia"),
        Map.entry("HY", "Պատմական"), Map.entry("FA", "تاریخی"), Map.entry("AR", "تاريخي"), Map.entry("HE", "היסטוריה"),
        Map.entry("TA", "வரலாறு"), Map.entry("TE", "చరిత్ర"), Map.entry("ML", "ചരിത്രം"), Map.entry("UR", "تاریخی"),
        Map.entry("TL", "Kasaysayan"), Map.entry("BS", "Istorijski"), Map.entry("SQ", "Histori"), Map.entry("AZ", "Tarixi"),
        Map.entry("KA", "ისტორიული"), Map.entry("MYN", "Historia")
    ));

    m.put("horror", Map.ofEntries(
        Map.entry("EN", "Horror"), Map.entry("EN_US", "Horror"), Map.entry("EN_GB", "Horror"), Map.entry("EN_AU", "Horror"),
        Map.entry("RU", "Ужасы"), Map.entry("FR", "Horreur"), Map.entry("FR_FR", "Horreur"), Map.entry("FR_CA", "Horreur"),
        Map.entry("DE", "Horror"), Map.entry("ES", "Terror"), Map.entry("ES_ES", "Terror"), Map.entry("ES_419", "Terror"),
        Map.entry("IT", "Horror"), Map.entry("PT", "Terror"), Map.entry("PT_BR", "Terror"), Map.entry("PT_PT", "Terror"),
        Map.entry("JA", "ホラー"), Map.entry("KO", "공포"),
        Map.entry("ZH", "恐怖"), Map.entry("ZH_HANS_CN", "恐怖"), Map.entry("ZH_HANT_TW", "恐怖"), Map.entry("ZH_HANT_HK", "恐怖"),
        Map.entry("HI", "भयानक"), Map.entry("BN", "ভৌতিক"), Map.entry("TR", "Korku"),
        Map.entry("EL", "Τρόμος"), Map.entry("TH", "สยองขวัญ"), Map.entry("ID", "Horor"), Map.entry("MS", "Seram"),
        Map.entry("VI", "Kinh dị"), Map.entry("DA", "Gyser"), Map.entry("SV", "Skräck"), Map.entry("NB", "Grøsser"),
        Map.entry("NL", "Horror"), Map.entry("NL_NL", "Horror"), Map.entry("NL_BE", "Horror"),
        Map.entry("PL", "Horror"), Map.entry("UK", "Жахи"), Map.entry("BE", "Жахі"), Map.entry("BG", "Ужаси"),
        Map.entry("CS", "Horor"), Map.entry("SK", "Horor"), Map.entry("SL", "Grozljivka"), Map.entry("HR", "Horor"),
        Map.entry("SR", "Хорор"), Map.entry("MK", "Хорор"), Map.entry("HU", "Horror"), Map.entry("RO", "Horror"),
        Map.entry("ET", "Õudusfilm"), Map.entry("LV", "Šausmu"), Map.entry("LT", "Siaubo"), Map.entry("FI", "Kauhu"),
        Map.entry("HY", "Սարսափ"), Map.entry("FA", "ترسناک"), Map.entry("AR", "رعب"), Map.entry("HE", "אימה"),
        Map.entry("TA", "திகில்"), Map.entry("TE", "భయానకం"), Map.entry("ML", "ഭീകരം"), Map.entry("UR", "خوفناک"),
        Map.entry("TL", "Horror"), Map.entry("BS", "Horor"), Map.entry("SQ", "Horror"), Map.entry("AZ", "Dəhşət"),
        Map.entry("KA", "საშინელება"), Map.entry("MYN", "Terror")
    ));

    m.put("music", Map.ofEntries(
        Map.entry("EN", "Music"), Map.entry("EN_US", "Music"), Map.entry("EN_GB", "Music"), Map.entry("EN_AU", "Music"),
        Map.entry("RU", "Музыка"), Map.entry("FR", "Musique"), Map.entry("FR_FR", "Musique"), Map.entry("FR_CA", "Musique"),
        Map.entry("DE", "Musik"), Map.entry("ES", "Música"), Map.entry("ES_ES", "Música"), Map.entry("ES_419", "Música"),
        Map.entry("IT", "Musica"), Map.entry("PT", "Música"), Map.entry("PT_BR", "Música"), Map.entry("PT_PT", "Música"),
        Map.entry("JA", "音楽"), Map.entry("KO", "음악"),
        Map.entry("ZH", "音乐"), Map.entry("ZH_HANS_CN", "音乐"), Map.entry("ZH_HANT_TW", "音樂"), Map.entry("ZH_HANT_HK", "音樂"),
        Map.entry("HI", "संगीत"), Map.entry("BN", "সংগীত"), Map.entry("TR", "Müzik"),
        Map.entry("EL", "Μουσική"), Map.entry("TH", "ดนตรี"), Map.entry("ID", "Musik"), Map.entry("MS", "Muzik"),
        Map.entry("VI", "Âm nhạc"), Map.entry("DA", "Musik"), Map.entry("SV", "Musik"), Map.entry("NB", "Musikk"),
        Map.entry("NL", "Muziek"), Map.entry("NL_NL", "Muziek"), Map.entry("NL_BE", "Muziek"),
        Map.entry("PL", "Muzyka"), Map.entry("UK", "Музика"), Map.entry("BE", "Музыка"), Map.entry("BG", "Музика"),
        Map.entry("CS", "Hudební"), Map.entry("SK", "Hudobný"), Map.entry("SL", "Glasbeni"), Map.entry("HR", "Glazbeni"),
        Map.entry("SR", "Музички"), Map.entry("MK", "Музика"), Map.entry("HU", "Zenei"), Map.entry("RO", "Muzică"),
        Map.entry("ET", "Muusikafilm"), Map.entry("LV", "Mūzika"), Map.entry("LT", "Muzikinis"), Map.entry("FI", "Musiikki"),
        Map.entry("HY", "Երաժշտական"), Map.entry("FA", "موسیقی"), Map.entry("AR", "موسيقى"), Map.entry("HE", "מוזיקה"),
        Map.entry("TA", "இசை"), Map.entry("TE", "సంగీతం"), Map.entry("ML", "സംഗീതം"), Map.entry("UR", "موسیقی"),
        Map.entry("TL", "Musika"), Map.entry("BS", "Muzički"), Map.entry("SQ", "Muzikë"), Map.entry("AZ", "Musiqi"),
        Map.entry("KA", "მუსიკა"), Map.entry("MYN", "Música")
    ));

    m.put("mystery", Map.ofEntries(
        Map.entry("EN", "Mystery"), Map.entry("EN_US", "Mystery"), Map.entry("EN_GB", "Mystery"), Map.entry("EN_AU", "Mystery"),
        Map.entry("RU", "Тайны"), Map.entry("FR", "Mystère"), Map.entry("FR_FR", "Mystère"), Map.entry("FR_CA", "Mystère"),
        Map.entry("DE", "Mystery"), Map.entry("ES", "Misterio"), Map.entry("ES_ES", "Misterio"), Map.entry("ES_419", "Misterio"),
        Map.entry("IT", "Mistero"), Map.entry("PT", "Mistério"), Map.entry("PT_BR", "Mistério"), Map.entry("PT_PT", "Mistério"),
        Map.entry("JA", "ミステリー"), Map.entry("KO", "미스터리"),
        Map.entry("ZH", "悬疑"), Map.entry("ZH_HANS_CN", "悬疑"), Map.entry("ZH_HANT_TW", "懸疑"), Map.entry("ZH_HANT_HK", "懸疑"),
        Map.entry("HI", "रहस्य"), Map.entry("BN", "রহস্য"), Map.entry("TR", "Gizem"),
        Map.entry("EL", "Μυστήριο"), Map.entry("TH", "ลึกลับ"), Map.entry("ID", "Misteri"), Map.entry("MS", "Misteri"),
        Map.entry("VI", "Bí ẩn"), Map.entry("DA", "Mysterium"), Map.entry("SV", "Mysterium"), Map.entry("NB", "Mysterium"),
        Map.entry("NL", "Mysterie"), Map.entry("NL_NL", "Mysterie"), Map.entry("NL_BE", "Mysterie"),
        Map.entry("PL", "Tajemnica"), Map.entry("UK", "Таємниці"), Map.entry("BE", "Таямніца"), Map.entry("BG", "Мистерия"),
        Map.entry("CS", "Mysteriózní"), Map.entry("SK", "Tajomný"), Map.entry("SL", "Skrivnost"), Map.entry("HR", "Misterij"),
        Map.entry("SR", "Мистерија"), Map.entry("MK", "Мистерија"), Map.entry("HU", "Misztikus"), Map.entry("RO", "Mister"),
        Map.entry("ET", "Müsteerium"), Map.entry("LV", "Noslēpums"), Map.entry("LT", "Mistinis"), Map.entry("FI", "Mysteeri"),
        Map.entry("HY", "Խորհրդավոր"), Map.entry("FA", "رمز و راز"), Map.entry("AR", "غموض"), Map.entry("HE", "מסתורין"),
        Map.entry("TA", "மர்மம்"), Map.entry("TE", "రహస్యం"), Map.entry("ML", "രഹസ്യം"), Map.entry("UR", "اسرار"),
        Map.entry("TL", "Misteryo"), Map.entry("BS", "Misterija"), Map.entry("SQ", "Mister"), Map.entry("AZ", "Sirr"),
        Map.entry("KA", "მისტიკა"), Map.entry("MYN", "Misterio")
    ));

    m.put("romance", Map.ofEntries(
        Map.entry("EN", "Romance"), Map.entry("EN_US", "Romance"), Map.entry("EN_GB", "Romance"), Map.entry("EN_AU", "Romance"),
        Map.entry("RU", "Романтика"), Map.entry("FR", "Romance"), Map.entry("FR_FR", "Romance"), Map.entry("FR_CA", "Romance"),
        Map.entry("DE", "Romantik"), Map.entry("ES", "Romance"), Map.entry("ES_ES", "Romance"), Map.entry("ES_419", "Romance"),
        Map.entry("IT", "Romantico"), Map.entry("PT", "Romance"), Map.entry("PT_BR", "Romance"), Map.entry("PT_PT", "Romance"),
        Map.entry("JA", "ロマンス"), Map.entry("KO", "로맨스"),
        Map.entry("ZH", "爱情"), Map.entry("ZH_HANS_CN", "爱情"), Map.entry("ZH_HANT_TW", "愛情"), Map.entry("ZH_HANT_HK", "愛情"),
        Map.entry("HI", "प्रेम"), Map.entry("BN", "রোমান্স"), Map.entry("TR", "Romantik"),
        Map.entry("EL", "Ρομαντισμός"), Map.entry("TH", "โรแมนติก"), Map.entry("ID", "Romantis"), Map.entry("MS", "Romantik"),
        Map.entry("VI", "Lãng mạn"), Map.entry("DA", "Romance"), Map.entry("SV", "Romantik"), Map.entry("NB", "Romantikk"),
        Map.entry("NL", "Romantiek"), Map.entry("NL_NL", "Romantiek"), Map.entry("NL_BE", "Romantiek"),
        Map.entry("PL", "Romans"), Map.entry("UK", "Романтика"), Map.entry("BE", "Рамантыка"), Map.entry("BG", "Романтика"),
        Map.entry("CS", "Romantický"), Map.entry("SK", "Romantický"), Map.entry("SL", "Romantika"), Map.entry("HR", "Romantika"),
        Map.entry("SR", "Романтика"), Map.entry("MK", "Романтика"), Map.entry("HU", "Romantikus"), Map.entry("RO", "Romantic"),
        Map.entry("ET", "Romantiline"), Map.entry("LV", "Romantika"), Map.entry("LT", "Romantinis"), Map.entry("FI", "Romantiikka"),
        Map.entry("HY", "Ռոմանտիկ"), Map.entry("FA", "رمانتیک"), Map.entry("AR", "رومانسي"), Map.entry("HE", "רומנטיקה"),
        Map.entry("TA", "காதல்"), Map.entry("TE", "రొమాన్స్"), Map.entry("ML", "റൊമാൻസ്"), Map.entry("UR", "رومانس"),
        Map.entry("TL", "Romansa"), Map.entry("BS", "Romantika"), Map.entry("SQ", "Romantikë"), Map.entry("AZ", "Romantik"),
        Map.entry("KA", "რომანტიკა"), Map.entry("MYN", "Romance")
    ));

    m.put("science-fiction", Map.ofEntries(
        Map.entry("EN", "Science Fiction"), Map.entry("EN_US", "Science Fiction"), Map.entry("EN_GB", "Science Fiction"), Map.entry("EN_AU", "Science Fiction"),
        Map.entry("RU", "Научная фантастика"), Map.entry("FR", "Science-fiction"), Map.entry("FR_FR", "Science-fiction"), Map.entry("FR_CA", "Science-fiction"),
        Map.entry("DE", "Science-Fiction"), Map.entry("ES", "Ciencia ficción"), Map.entry("ES_ES", "Ciencia ficción"), Map.entry("ES_419", "Ciencia ficción"),
        Map.entry("IT", "Fantascienza"), Map.entry("PT", "Ficção científica"), Map.entry("PT_BR", "Ficção científica"), Map.entry("PT_PT", "Ficção científica"),
        Map.entry("JA", "SF"), Map.entry("KO", "SF"),
        Map.entry("ZH", "科幻"), Map.entry("ZH_HANS_CN", "科幻"), Map.entry("ZH_HANT_TW", "科幻"), Map.entry("ZH_HANT_HK", "科幻"),
        Map.entry("HI", "विज्ञान कथा"), Map.entry("BN", "বিজ্ঞান কল্পকাহিনী"), Map.entry("TR", "Bilim kurgu"),
        Map.entry("EL", "Επιστημονική φαντασία"), Map.entry("TH", "นิยายวิทยาศาสตร์"), Map.entry("ID", "Fiksi ilmiah"), Map.entry("MS", "Fiksyen sains"),
        Map.entry("VI", "Khoa học viễn tưởng"), Map.entry("DA", "Science fiction"), Map.entry("SV", "Sci-fi"), Map.entry("NB", "Science fiction"),
        Map.entry("NL", "Sciencefiction"), Map.entry("NL_NL", "Sciencefiction"), Map.entry("NL_BE", "Sciencefiction"),
        Map.entry("PL", "Science fiction"), Map.entry("UK", "Наукова фантастика"), Map.entry("BE", "Навуковая фантастыка"), Map.entry("BG", "Научна фантастика"),
        Map.entry("CS", "Sci-fi"), Map.entry("SK", "Sci-fi"), Map.entry("SL", "Znanstvena fantastika"), Map.entry("HR", "Znanstvena fantastika"),
        Map.entry("SR", "Научна фантастика"), Map.entry("MK", "Научна фантастика"), Map.entry("HU", "Tudományos-fantasztikus"), Map.entry("RO", "Science fiction"),
        Map.entry("ET", "Ulme"), Map.entry("LV", "Zinātniskā fantastika"), Map.entry("LT", "Mokslinė fantastika"), Map.entry("FI", "Tieteiskirjallisuus"),
        Map.entry("HY", "Գիտական ֆանտաստիկա"), Map.entry("FA", "علمی-تخیلی"), Map.entry("AR", "خيال علمي"), Map.entry("HE", "מדע בדיוני"),
        Map.entry("TA", "அறிவியல் புனைவு"), Map.entry("TE", "సైన్స్ ఫిక్షన్"), Map.entry("ML", "ശാസ്ത്ര ഫിക്ഷൻ"), Map.entry("UR", "سائنس فکشن"),
        Map.entry("TL", "Agham Pantasya"), Map.entry("BS", "Naučna fantastika"), Map.entry("SQ", "Fantastikë shkencore"), Map.entry("AZ", "Elmi fantastika"),
        Map.entry("KA", "სამეცნიერო ფანტასტიკა"), Map.entry("MYN", "Ciencia ficción")
    ));

    m.put("tv-movie", Map.ofEntries(
        Map.entry("EN", "TV Movie"), Map.entry("EN_US", "TV Movie"), Map.entry("EN_GB", "TV Movie"), Map.entry("EN_AU", "TV Movie"),
        Map.entry("RU", "ТВ-фильм"), Map.entry("FR", "Téléfilm"), Map.entry("FR_FR", "Téléfilm"), Map.entry("FR_CA", "Téléfilm"),
        Map.entry("DE", "TV-Film"), Map.entry("ES", "Película de TV"), Map.entry("ES_ES", "Película de TV"), Map.entry("ES_419", "Película de TV"),
        Map.entry("IT", "Film TV"), Map.entry("PT", "Filme para TV"), Map.entry("PT_BR", "Filme para TV"), Map.entry("PT_PT", "Filme para TV"),
        Map.entry("JA", "テレビ映画"), Map.entry("KO", "TV 영화"),
        Map.entry("ZH", "电视电影"), Map.entry("ZH_HANS_CN", "电视电影"), Map.entry("ZH_HANT_TW", "電視電影"), Map.entry("ZH_HANT_HK", "電視電影"),
        Map.entry("HI", "टीवी फिल्म"), Map.entry("BN", "টিভি মুভি"), Map.entry("TR", "TV filmi"),
        Map.entry("EL", "Τηλεταινία"), Map.entry("TH", "ภาพยนตร์ทีวี"), Map.entry("ID", "Film TV"), Map.entry("MS", "Filem TV"),
        Map.entry("VI", "Phim truyền hình"), Map.entry("DA", "TV-film"), Map.entry("SV", "TV-film"), Map.entry("NB", "TV-film"),
        Map.entry("NL", "Televisiefilm"), Map.entry("NL_NL", "Televisiefilm"), Map.entry("NL_BE", "Televisiefilm"),
        Map.entry("PL", "Film TV"), Map.entry("UK", "ТВ-фільм"), Map.entry("BE", "ТВ-фільм"), Map.entry("BG", "ТВ филм"),
        Map.entry("CS", "TV film"), Map.entry("SK", "TV film"), Map.entry("SL", "TV film"), Map.entry("HR", "TV film"),
        Map.entry("SR", "ТВ филм"), Map.entry("MK", "ТВ филм"), Map.entry("HU", "TV-film"), Map.entry("RO", "Film TV"),
        Map.entry("ET", "TV-film"), Map.entry("LV", "TV filma"), Map.entry("LT", "TV filmas"), Map.entry("FI", "TV-elokuva"),
        Map.entry("HY", "Հեռուստաֆիլմ"), Map.entry("FA", "فیلم تلویزیونی"), Map.entry("AR", "فيلم تلفزيوني"), Map.entry("HE", "סרט טלוויזיה"),
        Map.entry("TA", "தொலைக்காட்சி திரைப்படம்"), Map.entry("TE", "టీవీ సినిమా"), Map.entry("ML", "ടിവി സിനിമ"), Map.entry("UR", "ٹی وی فلم"),
        Map.entry("TL", "Pelikulang TV"), Map.entry("BS", "TV film"), Map.entry("SQ", "Film TV"), Map.entry("AZ", "TV filmi"),
        Map.entry("KA", "სატელევიზიო ფილმი"), Map.entry("MYN", "Película de TV")
    ));

    m.put("thriller", Map.ofEntries(
        Map.entry("EN", "Thriller"), Map.entry("EN_US", "Thriller"), Map.entry("EN_GB", "Thriller"), Map.entry("EN_AU", "Thriller"),
        Map.entry("RU", "Триллер"), Map.entry("FR", "Thriller"), Map.entry("FR_FR", "Thriller"), Map.entry("FR_CA", "Thriller"),
        Map.entry("DE", "Thriller"), Map.entry("ES", "Suspenso"), Map.entry("ES_ES", "Thriller"), Map.entry("ES_419", "Suspenso"),
        Map.entry("IT", "Thriller"), Map.entry("PT", "Thriller"), Map.entry("PT_BR", "Suspense"), Map.entry("PT_PT", "Thriller"),
        Map.entry("JA", "スリラー"), Map.entry("KO", "스릴러"),
        Map.entry("ZH", "惊悚"), Map.entry("ZH_HANS_CN", "惊悚"), Map.entry("ZH_HANT_TW", "驚悚"), Map.entry("ZH_HANT_HK", "驚悚"),
        Map.entry("HI", "थ्रिलर"), Map.entry("BN", "থ্রিলার"), Map.entry("TR", "Gerilim"),
        Map.entry("EL", "Θρίλερ"), Map.entry("TH", "ระทึกขวัญ"), Map.entry("ID", "Thriller"), Map.entry("MS", "Thriller"),
        Map.entry("VI", "Ly kỳ"), Map.entry("DA", "Thriller"), Map.entry("SV", "Thriller"), Map.entry("NB", "Thriller"),
        Map.entry("NL", "Thriller"), Map.entry("NL_NL", "Thriller"), Map.entry("NL_BE", "Thriller"),
        Map.entry("PL", "Thriller"), Map.entry("UK", "Трилер"), Map.entry("BE", "Трылер"), Map.entry("BG", "Трилър"),
        Map.entry("CS", "Thriller"), Map.entry("SK", "Thriller"), Map.entry("SL", "Triler"), Map.entry("HR", "Triler"),
        Map.entry("SR", "Трилер"), Map.entry("MK", "Трилер"), Map.entry("HU", "Thriller"), Map.entry("RO", "Thriller"),
        Map.entry("ET", "Põnevusfilm"), Map.entry("LV", "Trilleris"), Map.entry("LT", "Trileris"), Map.entry("FI", "Trilleri"),
        Map.entry("HY", "Թրիլլեր"), Map.entry("FA", "هیجان‌انگیز"), Map.entry("AR", "إثارة"), Map.entry("HE", "מותחן"),
        Map.entry("TA", "ஆர்வமூட்டும்"), Map.entry("TE", "థ్రిల్లర్"), Map.entry("ML", "ത്രില്ലർ"), Map.entry("UR", "سنسنی خیز"),
        Map.entry("TL", "Thriller"), Map.entry("BS", "Triler"), Map.entry("SQ", "Thriller"), Map.entry("AZ", "Triller"),
        Map.entry("KA", "თრილერი"), Map.entry("MYN", "Suspenso")
    ));

    m.put("war", Map.ofEntries(
        Map.entry("EN", "War"), Map.entry("EN_US", "War"), Map.entry("EN_GB", "War"), Map.entry("EN_AU", "War"),
        Map.entry("RU", "Война"), Map.entry("FR", "Guerre"), Map.entry("FR_FR", "Guerre"), Map.entry("FR_CA", "Guerre"),
        Map.entry("DE", "Krieg"), Map.entry("ES", "Guerra"), Map.entry("ES_ES", "Guerra"), Map.entry("ES_419", "Guerra"),
        Map.entry("IT", "Guerra"), Map.entry("PT", "Guerra"), Map.entry("PT_BR", "Guerra"), Map.entry("PT_PT", "Guerra"),
        Map.entry("JA", "戦争"), Map.entry("KO", "전쟁"),
        Map.entry("ZH", "战争"), Map.entry("ZH_HANS_CN", "战争"), Map.entry("ZH_HANT_TW", "戰爭"), Map.entry("ZH_HANT_HK", "戰爭"),
        Map.entry("HI", "युद्ध"), Map.entry("BN", "যুদ্ধ"), Map.entry("TR", "Savaş"),
        Map.entry("EL", "Πόλεμος"), Map.entry("TH", "สงคราม"), Map.entry("ID", "Perang"), Map.entry("MS", "Perang"),
        Map.entry("VI", "Chiến tranh"), Map.entry("DA", "Krig"), Map.entry("SV", "Krig"), Map.entry("NB", "Krig"),
        Map.entry("NL", "Oorlog"), Map.entry("NL_NL", "Oorlog"), Map.entry("NL_BE", "Oorlog"),
        Map.entry("PL", "Wojenny"), Map.entry("UK", "Війна"), Map.entry("BE", "Вайна"), Map.entry("BG", "Война"),
        Map.entry("CS", "Válečný"), Map.entry("SK", "Vojnový"), Map.entry("SL", "Vojni"), Map.entry("HR", "Ratni"),
        Map.entry("SR", "Ратни"), Map.entry("MK", "Воен"), Map.entry("HU", "Háborús"), Map.entry("RO", "Război"),
        Map.entry("ET", "Sõjafilm"), Map.entry("LV", "Kara"), Map.entry("LT", "Karinis"), Map.entry("FI", "Sota"),
        Map.entry("HY", "Պատերազմ"), Map.entry("FA", "جنگی"), Map.entry("AR", "حرب"), Map.entry("HE", "מלחמה"),
        Map.entry("TA", "போர்"), Map.entry("TE", "యుద్ధం"), Map.entry("ML", "യുദ്ധം"), Map.entry("UR", "جنگی"),
        Map.entry("TL", "Digmaan"), Map.entry("BS", "Ratni"), Map.entry("SQ", "Luftë"), Map.entry("AZ", "Müharibə"),
        Map.entry("KA", "სამხედრო"), Map.entry("MYN", "Guerra")
    ));

    m.put("western", Map.ofEntries(
        Map.entry("EN", "Western"), Map.entry("EN_US", "Western"), Map.entry("EN_GB", "Western"), Map.entry("EN_AU", "Western"),
        Map.entry("RU", "Вестерн"), Map.entry("FR", "Western"), Map.entry("FR_FR", "Western"), Map.entry("FR_CA", "Western"),
        Map.entry("DE", "Western"), Map.entry("ES", "Western"), Map.entry("ES_ES", "Western"), Map.entry("ES_419", "Western"),
        Map.entry("IT", "Western"), Map.entry("PT", "Faroeste"), Map.entry("PT_BR", "Faroeste"), Map.entry("PT_PT", "Western"),
        Map.entry("JA", "西部劇"), Map.entry("KO", "서부"),
        Map.entry("ZH", "西部"), Map.entry("ZH_HANS_CN", "西部"), Map.entry("ZH_HANT_TW", "西部"), Map.entry("ZH_HANT_HK", "西部"),
        Map.entry("HI", "पश्चिमी"), Map.entry("BN", "পশ্চিমা"), Map.entry("TR", "Kovboy"),
        Map.entry("EL", "Γουέστερν"), Map.entry("TH", "คาวบอย"), Map.entry("ID", "Koboi"), Map.entry("MS", "Koboi"),
        Map.entry("VI", "Cao bồi"), Map.entry("DA", "Western"), Map.entry("SV", "Western"), Map.entry("NB", "Western"),
        Map.entry("NL", "Western"), Map.entry("NL_NL", "Western"), Map.entry("NL_BE", "Western"),
        Map.entry("PL", "Western"), Map.entry("UK", "Вестерн"), Map.entry("BE", "Вестэрн"), Map.entry("BG", "Уестърн"),
        Map.entry("CS", "Western"), Map.entry("SK", "Western"), Map.entry("SL", "Vestern"), Map.entry("HR", "Vestern"),
        Map.entry("SR", "Вестерн"), Map.entry("MK", "Вестерн"), Map.entry("HU", "Western"), Map.entry("RO", "Western"),
        Map.entry("ET", "Vestern"), Map.entry("LV", "Vesternu"), Map.entry("LT", "Vesternas"), Map.entry("FI", "Western"),
        Map.entry("HY", "Վեստերն"), Map.entry("FA", "وسترن"), Map.entry("AR", "غربي"), Map.entry("HE", "וסטרן"),
        Map.entry("TA", "மேற்கத்திய"), Map.entry("TE", "వెస్టర్న్"), Map.entry("ML", "വെസ്റ്റേൺ"), Map.entry("UR", "مغربی"),
        Map.entry("TL", "Kanluranin"), Map.entry("BS", "Vestern"), Map.entry("SQ", "Western"), Map.entry("AZ", "Vəstern"),
        Map.entry("KA", "ვესტერნი"), Map.entry("MYN", "Western")
    ));

    m.put("action-adventure", Map.ofEntries(
        Map.entry("EN", "Action & Adventure"), Map.entry("EN_US", "Action & Adventure"), Map.entry("EN_GB", "Action & Adventure"), Map.entry("EN_AU", "Action & Adventure"),
        Map.entry("RU", "Боевик и приключения"), Map.entry("FR", "Action et aventure"), Map.entry("FR_FR", "Action et aventure"), Map.entry("FR_CA", "Action et aventure"),
        Map.entry("DE", "Action & Abenteuer"), Map.entry("ES", "Acción y aventura"), Map.entry("ES_ES", "Acción y aventura"), Map.entry("ES_419", "Acción y aventura"),
        Map.entry("IT", "Azione e avventura"), Map.entry("PT", "Ação e aventura"), Map.entry("PT_BR", "Ação e aventura"), Map.entry("PT_PT", "Ação e aventura"),
        Map.entry("JA", "アクション＆アドベンチャー"), Map.entry("KO", "액션 & 어드벤처"),
        Map.entry("ZH", "动作冒险"), Map.entry("ZH_HANS_CN", "动作冒险"), Map.entry("ZH_HANT_TW", "動作冒險"), Map.entry("ZH_HANT_HK", "動作冒險"),
        Map.entry("HI", "एक्शन और रोमांच"), Map.entry("BN", "অ্যাকশন এবং অ্যাডভেঞ্চার"), Map.entry("TR", "Aksiyon ve macera"),
        Map.entry("EL", "Δράση & Περιπέτεια"), Map.entry("TH", "แอคชั่นและผจญภัย"), Map.entry("ID", "Aksi dan petualangan"), Map.entry("MS", "Aksi dan pengembaraan"),
        Map.entry("VI", "Hành động và phiêu lưu"), Map.entry("DA", "Action og eventyr"), Map.entry("SV", "Action och äventyr"), Map.entry("NB", "Action og eventyr"),
        Map.entry("NL", "Actie en avontuur"), Map.entry("NL_NL", "Actie en avontuur"), Map.entry("NL_BE", "Actie en avontuur"),
        Map.entry("PL", "Akcja i przygoda"), Map.entry("UK", "Бойовик і пригоди"), Map.entry("BE", "Баявік і прыгоды"), Map.entry("BG", "Екшън и приключение"),
        Map.entry("CS", "Akční a dobrodružný"), Map.entry("SK", "Akčný a dobrodružný"), Map.entry("SL", "Akcija in pustolovščina"), Map.entry("HR", "Akcija i pustolovine"),
        Map.entry("SR", "Акција и авантура"), Map.entry("MK", "Акција и авантура"), Map.entry("HU", "Akció és kaland"), Map.entry("RO", "Acțiune și aventură"),
        Map.entry("ET", "Märuli ja seiklus"), Map.entry("LV", "Asa sižeta un piedzīvojumu"), Map.entry("LT", "Veiksmo ir nuotykių"), Map.entry("FI", "Toiminta ja seikkailu"),
        Map.entry("HY", "Բոևական և արկածային"), Map.entry("FA", "اکشن و ماجرایی"), Map.entry("AR", "أكشن ومغامرة"), Map.entry("HE", "אקשן והרפתקאות"),
        Map.entry("TA", "சாகசமும் சாகசப் பயணமும்"), Map.entry("TE", "యాక్షన్ మరియు సాహసం"), Map.entry("ML", "ആക്ഷനും സാഹസവും"), Map.entry("UR", "ایکشن اور مہم جوئی"),
        Map.entry("TL", "Aksyon at Pakikipagsapalaran"), Map.entry("BS", "Akcija i pustolovine"), Map.entry("SQ", "Aksion dhe aventurë"), Map.entry("AZ", "Döyüş və macəra"),
        Map.entry("KA", "სამოქმედო და სათავგადასავლო"), Map.entry("MYN", "Acción y aventura")
    ));

    m.put("kids", Map.ofEntries(
        Map.entry("EN", "Kids"), Map.entry("EN_US", "Kids"), Map.entry("EN_GB", "Kids"), Map.entry("EN_AU", "Kids"),
        Map.entry("RU", "Детское"), Map.entry("FR", "Enfants"), Map.entry("FR_FR", "Enfants"), Map.entry("FR_CA", "Enfants"),
        Map.entry("DE", "Kinder"), Map.entry("ES", "Infantil"), Map.entry("ES_ES", "Infantil"), Map.entry("ES_419", "Infantil"),
        Map.entry("IT", "Per bambini"), Map.entry("PT", "Infantil"), Map.entry("PT_BR", "Infantil"), Map.entry("PT_PT", "Infantil"),
        Map.entry("JA", "キッズ"), Map.entry("KO", "어린이"),
        Map.entry("ZH", "儿童"), Map.entry("ZH_HANS_CN", "儿童"), Map.entry("ZH_HANT_TW", "兒童"), Map.entry("ZH_HANT_HK", "兒童"),
        Map.entry("HI", "बच्चों के लिए"), Map.entry("BN", "শিশু"), Map.entry("TR", "Çocuk"),
        Map.entry("EL", "Παιδικά"), Map.entry("TH", "เด็ก"), Map.entry("ID", "Anak-anak"), Map.entry("MS", "Kanak-kanak"),
        Map.entry("VI", "Thiếu nhi"), Map.entry("DA", "Børn"), Map.entry("SV", "Barn"), Map.entry("NB", "Barn"),
        Map.entry("NL", "Kinderen"), Map.entry("NL_NL", "Kinderen"), Map.entry("NL_BE", "Kinderen"),
        Map.entry("PL", "Dla dzieci"), Map.entry("UK", "Дитяче"), Map.entry("BE", "Дзіцячае"), Map.entry("BG", "Детско"),
        Map.entry("CS", "Pro děti"), Map.entry("SK", "Pre deti"), Map.entry("SL", "Za otroke"), Map.entry("HR", "Za djecu"),
        Map.entry("SR", "За децу"), Map.entry("MK", "За деца"), Map.entry("HU", "Gyerekeknek"), Map.entry("RO", "Copii"),
        Map.entry("ET", "Lastefilm"), Map.entry("LV", "Bērniem"), Map.entry("LT", "Vaikams"), Map.entry("FI", "Lapset"),
        Map.entry("HY", "Մանկական"), Map.entry("FA", "کودکان"), Map.entry("AR", "أطفال"), Map.entry("HE", "ילדים"),
        Map.entry("TA", "குழந்தைகள்"), Map.entry("TE", "పిల్లలు"), Map.entry("ML", "കുട്ടികൾ"), Map.entry("UR", "بچوں کے لیے"),
        Map.entry("TL", "Bata"), Map.entry("BS", "Za djecu"), Map.entry("SQ", "Fëmijë"), Map.entry("AZ", "Uşaqlar"),
        Map.entry("KA", "საბავშვო"), Map.entry("MYN", "Niños")
    ));

    m.put("news", Map.ofEntries(
        Map.entry("EN", "News"), Map.entry("EN_US", "News"), Map.entry("EN_GB", "News"), Map.entry("EN_AU", "News"),
        Map.entry("RU", "Новости"), Map.entry("FR", "Actualités"), Map.entry("FR_FR", "Actualités"), Map.entry("FR_CA", "Actualités"),
        Map.entry("DE", "Nachrichten"), Map.entry("ES", "Noticias"), Map.entry("ES_ES", "Noticias"), Map.entry("ES_419", "Noticias"),
        Map.entry("IT", "Notizie"), Map.entry("PT", "Notícias"), Map.entry("PT_BR", "Notícias"), Map.entry("PT_PT", "Notícias"),
        Map.entry("JA", "ニュース"), Map.entry("KO", "뉴스"),
        Map.entry("ZH", "新闻"), Map.entry("ZH_HANS_CN", "新闻"), Map.entry("ZH_HANT_TW", "新聞"), Map.entry("ZH_HANT_HK", "新聞"),
        Map.entry("HI", "समाचार"), Map.entry("BN", "সংবাদ"), Map.entry("TR", "Haberler"),
        Map.entry("EL", "Ειδήσεις"), Map.entry("TH", "ข่าว"), Map.entry("ID", "Berita"), Map.entry("MS", "Berita"),
        Map.entry("VI", "Tin tức"), Map.entry("DA", "Nyheder"), Map.entry("SV", "Nyheter"), Map.entry("NB", "Nyheter"),
        Map.entry("NL", "Nieuws"), Map.entry("NL_NL", "Nieuws"), Map.entry("NL_BE", "Nieuws"),
        Map.entry("PL", "Wiadomości"), Map.entry("UK", "Новини"), Map.entry("BE", "Навіны"), Map.entry("BG", "Новини"),
        Map.entry("CS", "Zprávy"), Map.entry("SK", "Správy"), Map.entry("SL", "Novice"), Map.entry("HR", "Vijesti"),
        Map.entry("SR", "Вести"), Map.entry("MK", "Вести"), Map.entry("HU", "Hírek"), Map.entry("RO", "Știri"),
        Map.entry("ET", "Uudised"), Map.entry("LV", "Ziņas"), Map.entry("LT", "Naujienos"), Map.entry("FI", "Uutiset"),
        Map.entry("HY", "Լուրեր"), Map.entry("FA", "اخبار"), Map.entry("AR", "أخبار"), Map.entry("HE", "חדשות"),
        Map.entry("TA", "செய்திகள்"), Map.entry("TE", "వార్తలు"), Map.entry("ML", "വാർത്ത"), Map.entry("UR", "خبریں"),
        Map.entry("TL", "Balita"), Map.entry("BS", "Vijesti"), Map.entry("SQ", "Lajme"), Map.entry("AZ", "Xəbərlər"),
        Map.entry("KA", "სიახლეები"), Map.entry("MYN", "Noticias")
    ));

    m.put("reality", Map.ofEntries(
        Map.entry("EN", "Reality"), Map.entry("EN_US", "Reality"), Map.entry("EN_GB", "Reality"), Map.entry("EN_AU", "Reality"),
        Map.entry("RU", "Реалити"), Map.entry("FR", "Téléréalité"), Map.entry("FR_FR", "Téléréalité"), Map.entry("FR_CA", "Téléréalité"),
        Map.entry("DE", "Reality"), Map.entry("ES", "Reality"), Map.entry("ES_ES", "Reality"), Map.entry("ES_419", "Reality"),
        Map.entry("IT", "Reality"), Map.entry("PT", "Reality"), Map.entry("PT_BR", "Reality"), Map.entry("PT_PT", "Reality"),
        Map.entry("JA", "リアリティ"), Map.entry("KO", "리얼리티"),
        Map.entry("ZH", "真人秀"), Map.entry("ZH_HANS_CN", "真人秀"), Map.entry("ZH_HANT_TW", "真人秀"), Map.entry("ZH_HANT_HK", "真人秀"),
        Map.entry("HI", "रियलिटी"), Map.entry("BN", "রিয়েলিটি"), Map.entry("TR", "Gerçeklik"),
        Map.entry("EL", "Ριάλιτι"), Map.entry("TH", "เรียลลิตี้"), Map.entry("ID", "Realitas"), Map.entry("MS", "Realiti"),
        Map.entry("VI", "Thực tế"), Map.entry("DA", "Reality"), Map.entry("SV", "Reality"), Map.entry("NB", "Reality"),
        Map.entry("NL", "Reality"), Map.entry("NL_NL", "Reality"), Map.entry("NL_BE", "Reality"),
        Map.entry("PL", "Reality"), Map.entry("UK", "Реаліті"), Map.entry("BE", "Рэаліці"), Map.entry("BG", "Реалити"),
        Map.entry("CS", "Reality"), Map.entry("SK", "Reality"), Map.entry("SL", "Resničnostni šov"), Map.entry("HR", "Reality"),
        Map.entry("SR", "Риалити"), Map.entry("MK", "Риалити"), Map.entry("HU", "Valóság-show"), Map.entry("RO", "Reality"),
        Map.entry("ET", "Reaalsustelesaade"), Map.entry("LV", "Realitātes šovs"), Map.entry("LT", "Realybės šou"), Map.entry("FI", "Reality"),
        Map.entry("HY", "Ռեալիթի"), Map.entry("FA", "واقعیت"), Map.entry("AR", "واقعي"), Map.entry("HE", "ריאליטי"),
        Map.entry("TA", "உண்மை நிகழ்ச்சி"), Map.entry("TE", "రియాలిటీ"), Map.entry("ML", "റിയാലിറ്റി"), Map.entry("UR", "ریئلٹی"),
        Map.entry("TL", "Katotohanan"), Map.entry("BS", "Reality"), Map.entry("SQ", "Reality"), Map.entry("AZ", "Reality"),
        Map.entry("KA", "რეალიტი"), Map.entry("MYN", "Reality")
    ));

    m.put("sci-fi-fantasy", Map.ofEntries(
        Map.entry("EN", "Sci-Fi & Fantasy"), Map.entry("EN_US", "Sci-Fi & Fantasy"), Map.entry("EN_GB", "Sci-Fi & Fantasy"), Map.entry("EN_AU", "Sci-Fi & Fantasy"),
        Map.entry("RU", "Фантастика и фэнтези"), Map.entry("FR", "Sci-Fi et fantaisie"), Map.entry("FR_FR", "Sci-Fi et fantaisie"), Map.entry("FR_CA", "Sci-Fi et fantaisie"),
        Map.entry("DE", "Sci-Fi & Fantasy"), Map.entry("ES", "Ciencia ficción y fantasía"), Map.entry("ES_ES", "Ciencia ficción y fantasía"), Map.entry("ES_419", "Ciencia ficción y fantasía"),
        Map.entry("IT", "Fantascienza e fantasy"), Map.entry("PT", "Ficção científica e fantasia"), Map.entry("PT_BR", "Ficção científica e fantasia"), Map.entry("PT_PT", "Ficção científica e fantasia"),
        Map.entry("JA", "SF＆ファンタジー"), Map.entry("KO", "SF & 판타지"),
        Map.entry("ZH", "科幻奇幻"), Map.entry("ZH_HANS_CN", "科幻奇幻"), Map.entry("ZH_HANT_TW", "科幻奇幻"), Map.entry("ZH_HANT_HK", "科幻奇幻"),
        Map.entry("HI", "साइंस फाई और फंतासी"), Map.entry("BN", "সায়েন্স ফিকশন এবং ফ্যান্টাসি"), Map.entry("TR", "Bilim kurgu ve fantezi"),
        Map.entry("EL", "Επιστ. φαντασία & Φαντασία"), Map.entry("TH", "วิทยาศาสตร์และแฟนตาซี"), Map.entry("ID", "Fiksi ilmiah dan fantasi"), Map.entry("MS", "Fiksyen sains dan fantasi"),
        Map.entry("VI", "Khoa học viễn tưởng và kỳ ảo"), Map.entry("DA", "Sci-fi og fantasy"), Map.entry("SV", "Sci-fi och fantasy"), Map.entry("NB", "Sci-fi og fantasy"),
        Map.entry("NL", "Sci-fi en fantasy"), Map.entry("NL_NL", "Sci-fi en fantasy"), Map.entry("NL_BE", "Sci-fi en fantasy"),
        Map.entry("PL", "Sci-fi i fantasy"), Map.entry("UK", "Фантастика і фентезі"), Map.entry("BE", "Фантастыка і фэнтэзі"), Map.entry("BG", "Фантастика и фентъзи"),
        Map.entry("CS", "Sci-fi a fantasy"), Map.entry("SK", "Sci-fi a fantasy"), Map.entry("SL", "Sci-fi in fantazija"), Map.entry("HR", "Sci-fi i fantazija"),
        Map.entry("SR", "Sci-fi и фантазија"), Map.entry("MK", "Sci-fi и фантазија"), Map.entry("HU", "Sci-fi és fantasy"), Map.entry("RO", "Sci-fi și fantasy"),
        Map.entry("ET", "Ulme ja fantaasia"), Map.entry("LV", "Zinātniskā fantastika un fantāzija"), Map.entry("LT", "Mokslinė fantastika ir fantastika"), Map.entry("FI", "Sci-fi ja fantasia"),
        Map.entry("HY", "Գիտական ֆանտաստիկա և ֆանտազիա"), Map.entry("FA", "علمی تخیلی و فانتزی"), Map.entry("AR", "خيال علمي وخيال"), Map.entry("HE", "מדע בדיוני ופנטזיה"),
        Map.entry("TA", "அறிவியல் புனைவும் கற்பனையும்"), Map.entry("TE", "సైన్స్ ఫిక్షన్ మరియు కల్పన"), Map.entry("ML", "ശാസ്ത്ര ഫിക്ഷനും ഫാന്റസിയും"), Map.entry("UR", "سائنس فکشن اور فنتاسی"),
        Map.entry("TL", "Agham Pantasya at Pantasya"), Map.entry("BS", "Sci-fi i fantazija"), Map.entry("SQ", "Fantastikë shkencore dhe fantazi"), Map.entry("AZ", "Elmi fantastika və fantaziya"),
        Map.entry("KA", "სამეც. ფანტასტიკა და ფანტასტიკა"), Map.entry("MYN", "Ciencia ficción y fantasía")
    ));

    m.put("soap", Map.ofEntries(
        Map.entry("EN", "Soap"), Map.entry("EN_US", "Soap"), Map.entry("EN_GB", "Soap"), Map.entry("EN_AU", "Soap"),
        Map.entry("RU", "Мыльная опера"), Map.entry("FR", "Soap opéra"), Map.entry("FR_FR", "Soap opéra"), Map.entry("FR_CA", "Soap opéra"),
        Map.entry("DE", "Soap Opera"), Map.entry("ES", "Telenovela"), Map.entry("ES_ES", "Telenovela"), Map.entry("ES_419", "Telenovela"),
        Map.entry("IT", "Soap opera"), Map.entry("PT", "Soap opera"), Map.entry("PT_BR", "Novela"), Map.entry("PT_PT", "Soap opera"),
        Map.entry("JA", "ソープオペラ"), Map.entry("KO", "연속극"),
        Map.entry("ZH", "肥皂剧"), Map.entry("ZH_HANS_CN", "肥皂剧"), Map.entry("ZH_HANT_TW", "肥皂劇"), Map.entry("ZH_HANT_HK", "肥皂劇"),
        Map.entry("HI", "सोप ओपेरा"), Map.entry("BN", "সোপ অপেরা"), Map.entry("TR", "Pembe dizi"),
        Map.entry("EL", "Σαπουνόπερα"), Map.entry("TH", "ละครน้ำเน่า"), Map.entry("ID", "Opera sabun"), Map.entry("MS", "Opera sabun"),
        Map.entry("VI", "Phim dài tập"), Map.entry("DA", "Sæbeopera"), Map.entry("SV", "Såpopera"), Map.entry("NB", "Såpeopera"),
        Map.entry("NL", "Soap opera"), Map.entry("NL_NL", "Soap opera"), Map.entry("NL_BE", "Soap opera"),
        Map.entry("PL", "Opera mydlana"), Map.entry("UK", "Мильна опера"), Map.entry("BE", "Мыльная опера"), Map.entry("BG", "Сапунена опера"),
        Map.entry("CS", "Mýdlová opera"), Map.entry("SK", "Mýdlová opera"), Map.entry("SL", "Nadaljevanka"), Map.entry("HR", "Sapunica"),
        Map.entry("SR", "Сапуница"), Map.entry("MK", "Сапуница"), Map.entry("HU", "Szappanopera"), Map.entry("RO", "Telenovelă"),
        Map.entry("ET", "Seebiooper"), Map.entry("LV", "Seriāls"), Map.entry("LT", "Muilo opera"), Map.entry("FI", "Saippuaooppera"),
        Map.entry("HY", "Սաբոնային օպերա"), Map.entry("FA", "سریال"), Map.entry("AR", "مسلسل"), Map.entry("HE", "סבון"),
        Map.entry("TA", "தொடர்கதை"), Map.entry("TE", "సోప్ ఒపెరా"), Map.entry("ML", "സോപ്പ് ഒപ്പേര"), Map.entry("UR", "سوپ اوپیرا"),
        Map.entry("TL", "Telenovela"), Map.entry("BS", "Sapunica"), Map.entry("SQ", "Sapunore"), Map.entry("AZ", "Sabun operası"),
        Map.entry("KA", "სერიალი"), Map.entry("MYN", "Telenovela")
    ));

    m.put("talk", Map.ofEntries(
        Map.entry("EN", "Talk"), Map.entry("EN_US", "Talk"), Map.entry("EN_GB", "Talk"), Map.entry("EN_AU", "Talk"),
        Map.entry("RU", "Ток-шоу"), Map.entry("FR", "Talk-show"), Map.entry("FR_FR", "Talk-show"), Map.entry("FR_CA", "Talk-show"),
        Map.entry("DE", "Talkshow"), Map.entry("ES", "Programa de entrevistas"), Map.entry("ES_ES", "Programa de entrevistas"), Map.entry("ES_419", "Programa de entrevistas"),
        Map.entry("IT", "Talk show"), Map.entry("PT", "Talk show"), Map.entry("PT_BR", "Talk show"), Map.entry("PT_PT", "Talk show"),
        Map.entry("JA", "トーク"), Map.entry("KO", "토크"),
        Map.entry("ZH", "脱口秀"), Map.entry("ZH_HANS_CN", "脱口秀"), Map.entry("ZH_HANT_TW", "脫口秀"), Map.entry("ZH_HANT_HK", "脫口秀"),
        Map.entry("HI", "टॉक शो"), Map.entry("BN", "টক শো"), Map.entry("TR", "Sohbet programı"),
        Map.entry("EL", "Ομιλία"), Map.entry("TH", "รายการสนทนา"), Map.entry("ID", "Talkshow"), Map.entry("MS", "Talkshow"),
        Map.entry("VI", "Tọa đàm"), Map.entry("DA", "Talk"), Map.entry("SV", "Pratprogram"), Map.entry("NB", "Talkshow"),
        Map.entry("NL", "Praatprogramma"), Map.entry("NL_NL", "Praatprogramma"), Map.entry("NL_BE", "Praatprogramma"),
        Map.entry("PL", "Talk-show"), Map.entry("UK", "Ток-шоу"), Map.entry("BE", "Ток-шоу"), Map.entry("BG", "Ток шоу"),
        Map.entry("CS", "Talk show"), Map.entry("SK", "Talk show"), Map.entry("SL", "Pogovorni šov"), Map.entry("HR", "Talk show"),
        Map.entry("SR", "Ток шоу"), Map.entry("MK", "Ток шоу"), Map.entry("HU", "Talk-show"), Map.entry("RO", "Talk show"),
        Map.entry("ET", "Jutusaade"), Map.entry("LV", "Sarunu šovs"), Map.entry("LT", "Pokalbių laida"), Map.entry("FI", "Puheohjelma"),
        Map.entry("HY", "Թոք-շոու"), Map.entry("FA", "برنامه گفتگو"), Map.entry("AR", "برنامج حوار"), Map.entry("HE", "תוכנית שיחה"),
        Map.entry("TA", "பேச்சு நிகழ்ச்சி"), Map.entry("TE", "టాక్ షో"), Map.entry("ML", "ടോക് ഷോ"), Map.entry("UR", "ٹاک شو"),
        Map.entry("TL", "Pag-uusap"), Map.entry("BS", "Talk show"), Map.entry("SQ", "Bisedë"), Map.entry("AZ", "Söhbət şousu"),
        Map.entry("KA", "სამეტყველო შოუ"), Map.entry("MYN", "Programa de entrevistas")
    ));

    m.put("war-politics", Map.ofEntries(
        Map.entry("EN", "War & Politics"), Map.entry("EN_US", "War & Politics"), Map.entry("EN_GB", "War & Politics"), Map.entry("EN_AU", "War & Politics"),
        Map.entry("RU", "Война и политика"), Map.entry("FR", "Guerre et politique"), Map.entry("FR_FR", "Guerre et politique"), Map.entry("FR_CA", "Guerre et politique"),
        Map.entry("DE", "Krieg & Politik"), Map.entry("ES", "Guerra y política"), Map.entry("ES_ES", "Guerra y política"), Map.entry("ES_419", "Guerra y política"),
        Map.entry("IT", "Guerra e politica"), Map.entry("PT", "Guerra e política"), Map.entry("PT_BR", "Guerra e política"), Map.entry("PT_PT", "Guerra e política"),
        Map.entry("JA", "戦争＆政治"), Map.entry("KO", "전쟁 & 정치"),
        Map.entry("ZH", "战争政治"), Map.entry("ZH_HANS_CN", "战争政治"), Map.entry("ZH_HANT_TW", "戰爭政治"), Map.entry("ZH_HANT_HK", "戰爭政治"),
        Map.entry("HI", "युद्ध और राजनीति"), Map.entry("BN", "যুদ্ধ ও রাজনীতি"), Map.entry("TR", "Savaş ve politika"),
        Map.entry("EL", "Πόλεμος & Πολιτική"), Map.entry("TH", "สงครามและการเมือง"), Map.entry("ID", "Perang dan politik"), Map.entry("MS", "Perang dan politik"),
        Map.entry("VI", "Chiến tranh và chính trị"), Map.entry("DA", "Krig og politik"), Map.entry("SV", "Krig och politik"), Map.entry("NB", "Krig og politikk"),
        Map.entry("NL", "Oorlog en politiek"), Map.entry("NL_NL", "Oorlog en politiek"), Map.entry("NL_BE", "Oorlog en politiek"),
        Map.entry("PL", "Wojna i polityka"), Map.entry("UK", "Війна і політика"), Map.entry("BE", "Вайна і палітыка"), Map.entry("BG", "Война и политика"),
        Map.entry("CS", "Válka a politika"), Map.entry("SK", "Vojna a politika"), Map.entry("SL", "Vojna in politika"), Map.entry("HR", "Rat i politika"),
        Map.entry("SR", "Рат и политика"), Map.entry("MK", "Војна и политика"), Map.entry("HU", "Háború és politika"), Map.entry("RO", "Război și politică"),
        Map.entry("ET", "Sõda ja poliitika"), Map.entry("LV", "Karš un politika"), Map.entry("LT", "Karas ir politika"), Map.entry("FI", "Sota ja politiikka"),
        Map.entry("HY", "Պատերազմ և քաղաքականություն"), Map.entry("FA", "جنگ و سیاست"), Map.entry("AR", "حرب وسياسة"), Map.entry("HE", "מלחמה ופוליטיקה"),
        Map.entry("TA", "போரும் அரசியலும்"), Map.entry("TE", "యుద్ధం మరియు రాజకీయాలు"), Map.entry("ML", "യുദ്ധവും രാഷ്ട്രീയവും"), Map.entry("UR", "جنگ اور سیاست"),
        Map.entry("TL", "Digmaan at Pulitika"), Map.entry("BS", "Rat i politika"), Map.entry("SQ", "Luftë dhe politikë"), Map.entry("AZ", "Müharibə və siyasət"),
        Map.entry("KA", "ომი და პოლიტიკა"), Map.entry("MYN", "Guerra y política")
    ));

    return Collections.unmodifiableMap(m);
  }
}
