async function api(path, method, body) {
    const options = { method: method || 'GET' };
    if (body) {
        options.headers = { 'Content-Type': 'application/json' };
        options.body = JSON.stringify(body);
    }
    const res = await fetch(path, options);
    if (!res.ok) {
        let msg = 'Ошибка ' + res.status;
        try {
            const data = await res.json();
            if (data.message) msg = data.message;
        } catch (e) { }
        throw new Error(msg);
    }
    const text = await res.text();
    return text ? JSON.parse(text) : null;
}

function nav(active) {
    const links = [
        ['index.html',      'Фразы'],
        ['add-phrase.html', 'Добавить фразу'],
        ['topics.html',     'Тематики'],
        ['places.html',     'Места'],
        ['languages.html',  'Языки']
    ];
    let html = '<nav>';
    for (const [href, text] of links) {
        html += (href === active ? '<b>' + text + '</b>' : '<a href="' + href + '">' + text + '</a>')
            + ' | ';
    }
    html += '</nav><hr>';
    document.body.insertAdjacentHTML('afterbegin', html);
}