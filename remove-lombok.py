#!/usr/bin/env python3
import os
import re

# Diretórios para processar
dirs_to_process = [
    '/workspaces/srv-usuario-poc/src/main/java',
    '/workspaces/srv-usuario-poc/src/test/java'
]

# Padrões Lombok a remover
patterns_to_remove = [
    r'@Slf4j\s*\n',
    r'@RequiredArgsConstructor\s*\n',
    r'@Data\s*\n',
    r'@NoArgsConstructor\s*\n',
    r'@AllArgsConstructor\s*\n',
    r'@Builder\s*\n',
    r'import lombok\.\w+;\n',
    r'import lombok\.\*;\n',
]

def process_java_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # Remove Lombok annotations
        for pattern in patterns_to_remove:
            content = re.sub(pattern, '', content)
        
        # Only write if changed
        if content != original_content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✓ Updated: {filepath}")
            return True
        return False
    except Exception as e:
        print(f"✗ Error processing {filepath}: {e}")
        return False

# Process all Java files
count = 0
for root_dir in dirs_to_process:
    if os.path.exists(root_dir):
        for root, dirs, files in os.walk(root_dir):
            for file in files:
                if file.endswith('.java'):
                    filepath = os.path.join(root, file)
                    if process_java_file(filepath):
                        count += 1

print(f"\n✓ Processed {count} files")
