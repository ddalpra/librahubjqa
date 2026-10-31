const endpoint = '/graphql';

const query = `
  query {
    books {
      id
      title
      isbn
      available
      author { name surname }
      category { name }
    }
    authors { id name surname }
    members { id firstName lastName email }
    loans { id status loanDate dueDate }
  }
`;

async function graphqlRequest(queryString, variables = {}) {
  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    },
    body: JSON.stringify({ query: queryString, variables })
  });

  if (!response.ok) {
    throw new Error('GraphQL request failed');
  }

  const payload = await response.json();
  if (payload.errors) {
    throw new Error(payload.errors[0].message);
  }

  return payload.data;
}

function renderDashboard(data) {
  const books = data.books ?? [];
  const availableBooks = books.filter((book) => book.available).length;
  const activeLoans = (data.loans ?? []).filter((loan) => loan.status === 'ACTIVE').length;

  document.querySelector('#app').innerHTML = `
    <div class="page">
      <aside class="sidebar">
        <h1>Biblioteca Hub</h1>
        <nav>
          <a class="active" href="#">Dashboard</a>
          <a href="#">Libri</a>
          <a href="#">Prestiti</a>
          <a href="#">Utenti</a>
        </nav>
      </aside>

      <main class="content">
        <header class="topbar">
          <div>
            <p class="eyebrow">Pannello di gestione</p>
            <h2>Biblioteca</h2>
          </div>
        </header>

        <section class="stats">
          <div class="card stat">
            <span>Totale libri</span>
            <strong>${books.length}</strong>
          </div>
          <div class="card stat">
            <span>Disponibili</span>
            <strong>${availableBooks}</strong>
          </div>
          <div class="card stat">
            <span>Prestiti attivi</span>
            <strong>${activeLoans}</strong>
          </div>
          <div class="card stat">
            <span>Autori</span>
            <strong>${(data.authors ?? []).length}</strong>
          </div>
        </section>

        <section class="panel">
          <div class="panel-header">
            <h3>Catalogo</h3>
          </div>
          <div class="book-grid">
            ${books
              .map(
                (book) => `
                  <article class="book-card">
                    <span class="badge ${book.available ? 'ok' : 'busy'}">
                      ${book.available ? 'Disponibile' : 'Prestato'}
                    </span>
                    <h4>${book.title}</h4>
                    <p>${book.author?.name ?? 'Autore'} ${book.author?.surname ?? ''}</p>
                    <small>${book.category?.name ?? 'Generico'}</small>
                    <code>${book.isbn}</code>
                  </article>
                `
              )
              .join('') || '<p>Nessun libro presente.</p>'}
          </div>
        </section>
      </main>
    </div>
  `;
}

async function bootstrap() {
  try {
    const data = await graphqlRequest(query);
    renderDashboard(data);
  } catch (error) {
    document.querySelector('#app').innerHTML = `
      <div class="error-box">
        <h2>Errore di connessione</h2>
        <p>Il backend GraphQL non è disponibile o non è stato avviato.</p>
        <pre>${error.message}</pre>
      </div>
    `;
  }
}

bootstrap();
