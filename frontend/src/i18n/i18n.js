import i18n from "i18next";
import XHR from "i18next-xhr-backend";
import LanguageDetector from "i18next-browser-languagedetector";
import translations from "./translations";

i18n
  .use(XHR)
  .use(LanguageDetector)
  .init({
    fallbackLng: "en",
    debug: process.env.NODE_ENV === "development",

    interpolation: {
      escapeValue: false // not needed for react!!
    },

    // react i18next special options (optional)
    react: {
      wait: false, // set to true if you like to wait for loaded in every translated hoc
      nsMode: "default" // set it to fallback to let passed namespaces to translated hoc act as fallbacks
    },

    resources: translations
  });

export const changeLanguage = lng => {
  i18n.changeLanguage(lng);
};

export default i18n;
