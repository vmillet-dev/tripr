const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    baseUrl: "http://host.docker.internal:8080", // Your Angular app's URL
    specPattern: "cypress/e2e/**/*.cy.{js,jsx,ts,tsx}", // Adjust spec pattern if needed.
    setupNodeEvents() {
      // implement node event listeners here
    },
    // supportFile: "cypress/support/e2e.js",
    // videosFolder: "cypress/videos",
    // screenshotsFolder: "cypress/screenshots",
    // downloadsFolder: "cypress/downloads"
  },
  component: {
    devServer: {
      framework: "angular",
      bundler: "webpack",
    },
  },
});
