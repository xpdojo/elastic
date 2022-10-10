from load import batch_fetch


def test_batch():
    data = [
        {"index": {"_index": "my-index", "_id": 'ID001'}},
        {"price": 1_000, "name": "test1"},
        {"index": {"_index": "my-index", "_id": 'ID002'}},
        {"price": 2_000, "name": "test2"},
        {"index": {"_index": "my-index", "_id": 'ID001'}},
        {"price": 1_000, "name": "test1"},
        {"index": {"_index": "my-index", "_id": 'ID002'}},
        {"price": 2_000, "name": "test2"},
    ]

    for batch_data in batch_fetch(data, 2):
        assert len(batch_data) == 4
        assert batch_data[0]["index"]["_index"] == "my-index"
        assert batch_data[1]["price"] == 1_000
