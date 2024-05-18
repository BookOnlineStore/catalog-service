## Api first

| Enpoint         | Method   | Req.body | Status | Resp.body | Description                                   |
|:----------------|:---------|:---------|:-------|:----------|:----------------------------------------------|
| `/books`        | `GET`    |          | `200`  | `Book[]`  | Get all the books in the catalog.             |
| `/books/{isbn}` | `GET`    |          | `200`  | `Book`    | Get the book with the given ISBN.             |
|                 |          |          | `404`  |           | No book with the given ISBN exists.           |
| `/books`        | `POST`   | `Book`   | `201`  | `Book`    | Add a new book to the catalog.                |
|                 |          |          | `422`  |           | A book with the same ISBN already exists.     |
|                 |          |          | `400`  |           | Book to update is invalid                     |
| `/books/{isbn}` | `PUT`    | `Book`   | `200`  | `Book`    | Update the book with the given ISBN.          |
|                 |          |          | `404`  |           | No book with the given ISBN exists to update. |
|                 |          |          | `400`  |           | Book to update is invalid                     |
| `/books/{isbn}` | `DELETE` |          | `204`  |           | Delete the book with the given ISBN.          |
