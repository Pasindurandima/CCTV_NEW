-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Insert default categories
INSERT INTO categories (name, description, display_order, is_active, created_at, updated_at) VALUES
('CCTV Cameras', 'General CCTV surveillance cameras for home and business security.', 1, TRUE, NOW(), NOW()),
('Wireless Cameras', 'Wi-Fi, battery-powered and wireless smart security cameras.', 2, TRUE, NOW(), NOW()),
('IP Cameras', 'IP network cameras with remote monitoring and smart features.', 3, TRUE, NOW(), NOW()),
('Analog CCTV Cameras', 'Analog CCTV cameras including Turbo HD and ColorVu series.', 4, TRUE, NOW(), NOW()),
('PTZ Cameras', 'Pan-Tilt-Zoom cameras with remote control and auto tracking.', 5, TRUE, NOW(), NOW()),
('Dome Cameras', 'Indoor and outdoor dome-style security cameras.', 6, TRUE, NOW(), NOW()),
('Bullet Cameras', 'Bullet-style outdoor surveillance cameras.', 7, TRUE, NOW(), NOW()),
('Baby Monitor Cameras', 'Smart baby monitoring cameras with two-way audio.', 8, TRUE, NOW(), NOW()),
('4G Cameras', '4G LTE security cameras for locations without Wi-Fi.', 9, TRUE, NOW(), NOW()),
('Solar Cameras', 'Solar-powered wireless surveillance cameras.', 10, TRUE, NOW(), NOW()),
('DVR', 'Digital Video Recorders for analog CCTV systems.', 11, TRUE, NOW(), NOW()),
('NVR', 'Network Video Recorders for IP camera systems.', 12, TRUE, NOW(), NOW()),
('CCTV Packages', 'Complete CCTV packages including cameras, recorder and accessories.', 13, TRUE, NOW(), NOW()),
('Hard Drives', 'Surveillance hard disk drives for continuous recording.', 14, TRUE, NOW(), NOW()),
('Memory Cards', 'Memory cards for security cameras and recording devices.', 15, TRUE, NOW(), NOW()),
('CCTV Accessories', 'CCTV accessories including power supplies, brackets, cables and connectors.', 16, TRUE, NOW(), NOW()),
('Network Equipment', 'Networking equipment including switches, routers, PoE devices and cables.', 17, TRUE, NOW(), NOW()),
('Monitors', 'Professional CCTV monitoring displays and surveillance monitors.', 18, TRUE, NOW(), NOW()),
('UPS & Power Backup', 'UPS systems and power backup solutions for security equipment.', 19, TRUE, NOW(), NOW()),
('Power Banks', 'Portable power banks and backup charging devices.', 20, TRUE, NOW(), NOW()),
('Biometric Attendance Systems', 'Biometric attendance systems with fingerprint and face recognition.', 21, TRUE, NOW(), NOW());

-- Show created categories
SELECT * FROM categories ORDER BY display_order;
