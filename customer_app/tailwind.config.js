/** @type {import('tailwindcss').Config} */

const { nextui } = require("@nextui-org/react");

export default {
  content: [
    "./index.html", 
    "./src/**/*.{js,ts,jsx,tsx}",
    "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {},
  },
  darkMode: "class",
  plugins: [
    nextui({
      prefix: "nextui",// prefix for themes variables
      addCommonColors: false,
      defaulTheme: "light", // default theme from the themes object
      defaultExtandTheme: "light", // default theme to extend on custom themes
      layout: {},
      themes: {
        light: {
          layout: {},
          colors: {
            text: '#05070a',
            background: '#f8fbfc',
            primary: '#220f67',
            secondary: '#544464',
            accent: '#f8d99b',
          },
        },
        dark: {
          layout: {},
          colors: {
            text: '#f5f7fa',
            background: '#030607',
            primary: '#ab98f0',
            secondary: '#ab9bbb',
            accent: '#644507',
          },
        },
      }
    })
  ],
}

