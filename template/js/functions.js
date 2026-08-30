function showButtons() {
    var showOnlyReplace = false
    var showOnlyMissing = false

    const elements = document.body.getElementsByTagName("tr")

    for (const el of elements) {
        const status = el.dataset.status

        if (status == 'M') {
            showOnlyMissing = true
        } else if (status == 'R') {
            showOnlyReplace = true
        }

        if (showOnlyMissing && showOnlyReplace) {
            break;
        }
    }

    if (!showOnlyMissing && !showOnlyReplace) {
        return
    }

    const p = document.getElementById('filterButtons')

    const showAllFilterLabel = document.createElement('label')
    const showAllFilterRadio = document.createElement('input')
    showAllFilterRadio.type = 'radio'
    showAllFilterRadio.name = 'filter'
    showAllFilterRadio.checked = true
    showAllFilterRadio.onchange = function () {
        filter(true, false, false)
    }
    showAllFilterLabel.appendChild(showAllFilterRadio)
    showAllFilterLabel.appendChild(document.createTextNode('Все'))
    p.appendChild(showAllFilterLabel)

    if (showOnlyMissing) {
        const showOnlyMissingLabel = document.createElement('label')
        const showOnlyMissingRadio = document.createElement('input')
        showOnlyMissingRadio.type = 'radio'
        showOnlyMissingRadio.name = 'filter'
        showOnlyMissingRadio.checked = false
        showOnlyMissingRadio.onchange = function () {
            filter(false, true, false)
        }
        showOnlyMissingLabel.appendChild(showOnlyMissingRadio)
        showOnlyMissingLabel.appendChild(document.createTextNode('Манколист'))
        p.appendChild(showOnlyMissingLabel)
    }

    if (showOnlyReplace) {
        const showOnlyReplaceLabel = document.createElement('label')
        const showOnlyReplaceRadio = document.createElement('input')
        showOnlyReplaceRadio.type = 'radio'
        showOnlyReplaceRadio.name = 'filter'
        showOnlyReplaceRadio.checked = false
        showOnlyReplaceRadio.onchange = function () {
            filter(false, false, true)
        }
        showOnlyReplaceLabel.appendChild(showOnlyReplaceRadio)
        showOnlyReplaceLabel.appendChild(document.createTextNode('На замену'))
        p.appendChild(showOnlyReplaceLabel)
    }
}

function filter(showAll, showOnlyMissing, showOnlyReplace) {
    const elements = document.body.getElementsByTagName("tr")
    for (const el of elements) {
        const status = el.dataset.status

        if (status == undefined || showAll) {
            el.style.display = null
            continue
        }

        if (showOnlyMissing && status != 'M') {
            el.style.display = "none"
        } else if (showOnlyReplace && status != 'R') {
            el.style.display = "none"
        } else {
            el.style.display = null
        }
    }
}