const { nextui } = require("@nextui-org/react")

/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}",
        "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}",
        "./node_modules/flowbite-react/lib/**/*.js"
    ],
    theme: {
        extend: {},
    },
    darkMode: "class",
    plugins: [nextui(), require("flowbite/plugin"), require("@tailwindcss/forms")],
}
