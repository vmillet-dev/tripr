describe('Simple Test', () => {
  it('Visits the homepage and checks the title', () => {
    // Visit the base URL defined in cypress.config.ts (e.g., http://localhost:4200)
    cy.visit('/');

    // Assert that the page title contains 'My App'
    cy.title().should('include', 'Welcome to your new app! - Tripr');
  });
})
