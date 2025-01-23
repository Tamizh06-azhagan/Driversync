<?php
// Enable error reporting for debugging (remove in production)
ini_set('display_errors', 1);
error_reporting(E_ALL);

require_once 'db.php';

// Read POST data (login credentials)
$requestData = json_decode(file_get_contents('php://input'), true);

if (isset($requestData['username']) && isset($requestData['password'])) {
    $username = $requestData['username'];
    $password = $requestData['password'];

    // Prepare the SQL query to check if the user exists and the password matches
    $stmt = $conn->prepare("SELECT * FROM users WHERE username = ? LIMIT 1");
    if ($stmt) {
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $user = $result->fetch_assoc();

            // Verify password (use password_hash and password_verify for secure password storage)
            if (password_verify($password, $user['password'])) {
                $response = [
                    'status' => true,
                    'message' => 'Login successful.'
                ];
            } else {
                $response = [
                    'status' => false,
                    'message' => 'Invalid username or password.'
                ];
            }
        } else {
            // Username doesn't exist
            $response = [
                'status' => false,
                'message' => 'Invalid username or password.'
            ];
        }

        $stmt->close();
    } else {
        $response = [
            'status' => false,
            'message' => 'Failed to prepare SQL statement.'
        ];
    }
} else {
    // Missing username or password in the request
    $response = [
        'status' => false,
        'message' => 'Please provide both username and password.'
    ];
}

// Close the database connection
$conn->close();

// Set response header for JSON response
header('Content-Type: application/json');

// Output the response as JSON
echo json_encode($response);
?>
