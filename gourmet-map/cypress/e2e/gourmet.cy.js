describe('身内グルメマップ E2E Tests', () => {

  const password = 'qa202601';
  const timestamp = Date.now();
  const testShopName = `最高のお店_${timestamp}`;
  const testComment = `テストコメント_${timestamp}`;

  beforeEach(() => {
    // Custom command or login logic before each test
    // Assuming Spring Security keeps session if we login once,
    // but we can just login fresh or use cy.session
    cy.visit('/login');
    cy.get('input[name="password"]').type(password);
    cy.get('button[type="submit"]').click();
  });

  context('Desktop View', () => {
    beforeEach(() => {
      cy.viewport('macbook-13');
    });

    it('正常系：ログイン〜新規投稿〜一覧表示〜詳細表示確認', () => {
      // 1. 一覧ページから新規投稿画面へ
      cy.visit('/');
      cy.get('.floating-action').click();
      cy.url().should('include', '/form');

      // 2. フォーム入力
      cy.get('#name').type(testShopName);
      cy.get('#category').select('ラーメン');
      cy.get('#rating').type('5');
      cy.get('#budget').select('～1000円');
      cy.get('#hideawayLevel').select('高');
      cy.get('#comment').type(testComment);

      // 3. 送信
      cy.get('button[type="submit"]').click();

      // 4. 一覧画面に戻ったこと、投稿した店名が表示されていることを確認
      cy.url().should('eq', Cypress.config().baseUrl + '/');
      cy.contains('.shop-title', testShopName).should('be.visible');

      // 5. 詳細画面の確認
      cy.contains('.shop-title', testShopName).click();
      cy.url().should('include', '/detail/');
      cy.get('.detail-content').within(() => {
        cy.contains(testShopName);
        cy.contains('ラーメン');
        cy.contains('～1000円');
        cy.contains('高');
        cy.contains(testComment);
      });
    });

    it('異常系：必須項目未入力での投稿（クライアントサイド/サーバーサイド検証）', () => {
      cy.visit('/form');
      
      // Clear rating since it might have default or easily bypassed
      // But Cypress might run into HTML5 required validation which stops form submission
      // To test server-side, we would need to remove the "required" attribute via DOM manipulation in Cypress
      cy.get('form').invoke('removeAttr', 'novalidate');

      cy.get('form').then(($form) => {
        $form[0].setAttribute('novalidate', true);
      });

      // Submit empty form (bypassing HTML5 required via novalidate)
      cy.get('button[type="submit"]').click();

      // Ensure we are still on the form page due to validation errors
      cy.url().should('include', '/form');

      // Check for Thymeleaf generated error messages
      cy.contains('.error-message', '店名は必須です').should('be.visible');
      cy.contains('.error-message', 'カテゴリは必須です').should('be.visible');
      cy.contains('.error-message', '評価は必須です').should('be.visible');
      cy.contains('.error-message', '予算感は必須です').should('be.visible');
      cy.contains('.error-message', 'コメントは必須です').should('be.visible');
    });
  });

  context('Mobile View (Responsive Test)', () => {
    beforeEach(() => {
      // iPhone X viewport
      cy.viewport('iphone-x');
    });

    it('スマホサイズでも一覧から新規投稿へ遷移し、表示が崩れないこと', () => {
      cy.visit('/');
      
      // 検索フォームが縦に並ぶかなどの基本確認
      cy.get('.filter-form').should('be.visible');
      
      // Floating action button needs to be clickable
      cy.get('.floating-action').should('be.visible').click();
      
      cy.url().should('include', '/form');
      
      // スマホサイズでフォーム入力して戻る
      cy.get('#name').type('スマホから投稿');
      cy.get('.btn-secondary').click(); // キャンセルして一覧に戻るテスト
      
      cy.url().should('eq', Cypress.config().baseUrl + '/');
    });
  });
});
