const PurgeCSS = require('purgecss').PurgeCSS;
const fs = require('fs');

const purgeCSSResult = new PurgeCSS().purge({
  content: ['./pages/cart.html'],
  css: ['./assets/css/cart.css'],
});

purgeCSSResult.then(result => {
  result.forEach(({ file, css }) => {
    fs.writeFileSync(file, css);
  });
});