from flask import Flask, request, jsonify

app = Flask(__name__)


def fibonacci_recursive(n):
    if n <= 1:
        return n
    else:
        return fibonacci_recursive(n - 1) + fibonacci_recursive(n - 2)


@app.route('/fibonacci', methods=['GET'])
def get_fibonacci():
    n = request.args.get('n', default=0, type=int)
    if n < 0:
        return jsonify(error="n должно быть неотрицательным"), 400
    result = fibonacci_recursive(n)
    return jsonify(n=n, fibonacci=result)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
