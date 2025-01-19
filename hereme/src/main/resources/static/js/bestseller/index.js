const ALADIN_API_URL = "/api/bestsellers";

async function fetchBestsellers(categoryId = "Book", maxResults = 10) {
    const url = `${ALADIN_API_URL}?category=${categoryId}&maxResults=${maxResults}`;

    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const data = await response.json();
            return data.item;
        } else {
            console.error(`Fetch Error: ${response.statusText}`);
            return [];
        }
    } catch (error) {
        console.error(`Error: ${error.message}`);
        return [];
    }
}