const express = require('express');
const path = require('path');
var cors = require('cors');
const app = express();

// Serve static files from the pages directory
app.use(express.static(path.join(__dirname, 'pages')));

// Allow CORS
app.use(cors());

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'index.html'));
});

app.get('/register', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'register.html'));
});

app.get('/home', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'home.html'));
});

app.get('/details', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'detail.html'));
});

app.get('/user', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'user.html'));
});

app.get('/cart', (req, res) => {
    res.sendFile(path.join(__dirname, 'pages', 'cart.html'));
});

app.use(express.static(path.join(__dirname, '/')));

app.listen(3000, () => {
    console.log('Server is running on http://localhost:3000');
});